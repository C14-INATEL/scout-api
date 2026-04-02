package com.balbuena.Scout.service;

import com.balbuena.Scout.dto.Response;
import com.balbuena.Scout.exception.ScoutException;
import com.balbuena.Scout.model.*;
import com.balbuena.Scout.repository.PresidentRepository;
import com.balbuena.Scout.repository.MatchRepository;
import com.balbuena.Scout.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChampionshipService {

    private final PresidentRepository presidentRepository;
    private final MatchRepository matchRepository;
    private final PlayerRepository playerRepository;
    private final GameService gameService;

    @Transactional
    public List<Response.Match> generateSchedule() {
        gameService.validatePhase(GamePhase.CHAMPIONSHIP, GamePhase.TRANSFER_WINDOW);

        if (matchRepository.count() > 0) {
            throw new ScoutException("Tabela de jogos ja foi gerada.");
        }

        List<President> presidents = presidentRepository.findAll();
        if (presidents.size() < 2) {
            throw new ScoutException("Necessario ao menos 2 presidents.");
        }

        List<Match> matches = new ArrayList<>();
        int round = 1;

        for (int i = 0; i < presidents.size(); i++) {
            for (int j = i + 1; j < presidents.size(); j++) {
                matches.add(Match.builder().roundNumber(round)
                        .homePresident(presidents.get(i)).awayPresident(presidents.get(j)).build());
                matches.add(Match.builder().roundNumber(round + 1)
                        .homePresident(presidents.get(j)).awayPresident(presidents.get(i)).build());
                round++;
            }
        }

        matchRepository.saveAll(matches);
        log.info("📅 {} partidas geradas em {} rodadas", matches.size(), round - 1);
        return matches.stream().map(this::toMatchResponse).collect(Collectors.toList());
    }

    @Transactional
    public List<Response.Match> simulateRound(int roundNumber) {
        gameService.validatePhase(GamePhase.CHAMPIONSHIP, GamePhase.TRANSFER_WINDOW);

        List<Match> roundMatches = matchRepository.findByRoundNumberAndPlayedFalse(roundNumber);
        if (roundMatches.isEmpty()) {
            throw new ScoutException("Rodada " + roundNumber + " nao encontrada ou ja simulada.");
        }

        Random random = new Random();
        List<Response.Match> results = new ArrayList<>();

        for (Match match : roundMatches) {
            double homeStr = calcStrength(match.getHomePresident()) * 1.1;
            double awayStr = calcStrength(match.getAwayPresident());
            double total   = homeStr + awayStr;

            int homeGoals = simulateGoals(homeStr / total, random);
            int awayGoals = simulateGoals(awayStr / total, random);

            match.setHomeGoals(homeGoals);
            match.setAwayGoals(awayGoals);
            match.setPlayed(true);
            matchRepository.save(match);

            updatePresidentStats(match.getHomePresident(), homeGoals, awayGoals);
            updatePresidentStats(match.getAwayPresident(), awayGoals, homeGoals);
            distributeGoals(match.getHomePresident(), homeGoals, awayGoals, random);
            distributeGoals(match.getAwayPresident(), awayGoals, homeGoals, random);

            log.info("⚽ R{}: {} {} x {} {}", roundNumber,
                    match.getHomePresident().getName(), homeGoals,
                    awayGoals, match.getAwayPresident().getName());
            results.add(toMatchResponse(match));
        }

        GameState state = gameService.getGameState();
        if (roundNumber > state.getCurrentRound()) {
            state.setCurrentRound(roundNumber);
        }

        return results;
    }

    @Transactional
    public List<Response.Match> simulateAllPendingRounds() {
        gameService.validatePhase(GamePhase.CHAMPIONSHIP, GamePhase.TRANSFER_WINDOW);

        Map<Integer, List<Match>> roundMap = matchRepository.findAll().stream()
                .collect(Collectors.groupingBy(Match::getRoundNumber));

        List<Response.Match> allResults = new ArrayList<>();
        new ArrayList<>(roundMap.keySet()).stream().sorted().forEach(round -> {
            boolean hasUnplayed = roundMap.get(round).stream().anyMatch(m -> !m.isPlayed());
            if (hasUnplayed) allResults.addAll(simulateRound(round));
        });
        return allResults;
    }

    private double calcStrength(President president) {
        return president.getTeam().stream().mapToDouble(Player::getValue).sum();
    }

    private int simulateGoals(double probability, Random random) {
        double avg = 2.5 * probability * 2;
        double L = Math.exp(-avg);
        double p = 1.0;
        int k = 0;
        do { k++; p *= random.nextDouble(); } while (p > L);
        return Math.max(0, k - 1);
    }

    private void updatePresidentStats(President president, int goalsFor, int goalsAgainst) {
        president.setGoalsFor(president.getGoalsFor() + goalsFor);
        president.setGoalsAgainst(president.getGoalsAgainst() + goalsAgainst);
        if      (goalsFor > goalsAgainst) { president.setWins(president.getWins() + 1); president.setPoints(president.getPoints() + 3); }
        else if (goalsFor == goalsAgainst){ president.setDraws(president.getDraws() + 1); president.setPoints(president.getPoints() + 1); }
        else                              { president.setLosses(president.getLosses() + 1); }
        presidentRepository.save(president);
    }

    private void distributeGoals(President team, int goals, int conceded, Random random) {
        List<Player> outfield = team.getTeam().stream()
                .filter(p -> p.getPosition() != Position.GOALKEEPER).toList();
        List<Player> gks = team.getTeam().stream()
                .filter(p -> p.getPosition() == Position.GOALKEEPER).toList();

        for (int i = 0; i < goals; i++) {
            if (!outfield.isEmpty()) {
                List<Player> weighted = new ArrayList<>();
                for (Player p : outfield) {
                    int w = switch (p.getPosition()) { case FORWARD -> 4; case MIDFIELDER -> 3; default -> 2; };
                    for (int x = 0; x < w; x++) weighted.add(p);
                }
                Player scorer = weighted.get(random.nextInt(weighted.size()));
                scorer.setGoalsScored(scorer.getGoalsScored() + 1);
                scorer.setMatchesPlayed(scorer.getMatchesPlayed() + 1);
                playerRepository.save(scorer);
            }
        }
        if (!gks.isEmpty()) {
            Player gk = gks.get(0);
            gk.setGoalsConceded(gk.getGoalsConceded() + conceded);
            gk.setMatchesPlayed(gk.getMatchesPlayed() + 1);
            playerRepository.save(gk);
        }
    }

    public List<Response.Match> getMatchesByRound(int round) {
        return matchRepository.findByRoundNumber(round).stream()
                .map(this::toMatchResponse).collect(Collectors.toList());
    }

    public List<Response.Match> getAllMatches() {
        return matchRepository.findAll().stream()
                .sorted(Comparator.comparingInt(Match::getRoundNumber))
                .map(this::toMatchResponse).collect(Collectors.toList());
    }

    private Response.Match toMatchResponse(Match m) {
        String result = null;
        if (m.isPlayed()) {
            if      (m.getHomeGoals() > m.getAwayGoals()) result = m.getHomePresident().getName() + " venceu";
            else if (m.getHomeGoals() < m.getAwayGoals()) result = m.getAwayPresident().getName() + " venceu";
            else result = "Empate";
        }
        return Response.Match.builder()
                .id(m.getId()).roundNumber(m.getRoundNumber())
                .homePresident(m.getHomePresident().getName())
                .awayPresident(m.getAwayPresident().getName())
                .homeGoals(m.getHomeGoals()).awayGoals(m.getAwayGoals())
                .played(m.isPlayed()).result(result).build();
    }
}