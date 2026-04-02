package com.balbuena.Scout.service;

import com.balbuena.Scout.dto.Response;
import com.balbuena.Scout.exception.ScoutException;
import com.balbuena.Scout.model.*;
import com.balbuena.Scout.repository.PresidentRepository;
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
public class DraftLotteryService {

    private final PresidentRepository presidentRepository;
    private final PlayerRepository playerRepository;
    private final GameService gameService;

    @Transactional
    public String runLottery() {
        gameService.validatePhase(GamePhase.DRAFT_LOTTERY);

        List<President> presidents = presidentRepository.findAll();
        if (presidents.isEmpty()) {
            throw new ScoutException("Nenhum president cadastrado.");
        }

        List<President> needPlayers = presidents.stream()
                .filter(p -> !p.isTeamComplete())
                .collect(Collectors.toList());

        if (needPlayers.isEmpty()) {
            gameService.advanceToChampionship();
            return "✅ Todos os times ja estao completos! Campeonato iniciado!";
        }

        List<Player> pool = new ArrayList<>(playerRepository.findByAuctionPlayerFalseAndAvailableTrue());
        pool.addAll(playerRepository.findByAuctionPlayerTrue().stream()
                .filter(Player::isAvailable).toList());

        if (pool.isEmpty()) {
            throw new ScoutException("Nao ha jogadores disponiveis para sorteio.");
        }

        Collections.shuffle(pool);
        StringBuilder result = new StringBuilder("🎰 Resultado do Sorteio:\n\n");

        for (President president : needPlayers) {
            int slotsNeeded = 5 - president.getTeam().size();
            boolean needsGoalkeeper = !president.hasGoalkeeper();

            result.append("🎽 ").append(president.getName()).append(":\n");

            if (needsGoalkeeper && slotsNeeded > 0) {
                Player gk = pool.stream()
                        .filter(p -> p.getPosition() == Position.GOALKEEPER)
                        .findFirst().orElse(null);
                if (gk != null) {
                    assignPlayer(president, gk, pool);
                    slotsNeeded--;
                    result.append("  🧤 ").append(gk.getName()).append(" (GK) - R$ ").append(gk.getValue()).append("\n");
                }
            }

            Iterator<Player> it = pool.iterator();
            int assigned = 0;
            while (assigned < slotsNeeded && it.hasNext()) {
                Player p = it.next();
                if (p.getPresident() == null) {
                    assignPlayer(president, p, pool);
                    assigned++;
                    result.append("  ⚽ ").append(p.getName())
                          .append(" (").append(p.getPosition()).append(") - R$ ").append(p.getValue()).append("\n");
                }
            }
            result.append("\n");
        }

        result.append("✅ Sorteio concluido! Use POST /api/game/advance-to-championship para iniciar.");
        return result.toString();
    }

    private void assignPlayer(President president, Player player, List<Player> pool) {
        player.setPresident(president);
        player.setAvailable(false);
        playerRepository.save(player);
        pool.remove(player);
    }

    public List<Response.Player> getAvailableForLottery() {
        List<Player> available = new ArrayList<>(playerRepository.findByAuctionPlayerFalseAndAvailableTrue());
        available.addAll(playerRepository.findByAuctionPlayerTrue().stream()
                .filter(Player::isAvailable).toList());
        return available.stream()
                .sorted(Comparator.comparing(Player::getPosition).thenComparing(Player::getName))
                .map(p -> Response.Player.builder()
                        .id(p.getId()).name(p.getName()).position(p.getPosition())
                        .value(p.getValue()).available(p.isAvailable()).auctionPlayer(p.isAuctionPlayer())
                        .goalsScored(p.getGoalsScored()).goalsConceded(p.getGoalsConceded()).build())
                .collect(Collectors.toList());
    }
}