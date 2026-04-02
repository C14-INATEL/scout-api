package com.balbuena.Scout.service;

import com.balbuena.Scout.dto.Request;
import com.balbuena.Scout.dto.Response;
import com.balbuena.Scout.exception.ScoutException;
import com.balbuena.Scout.model.*;
import com.balbuena.Scout.repository.AuctionBidRepository;
import com.balbuena.Scout.repository.PresidentRepository;
import com.balbuena.Scout.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuctionService {

    private final AuctionBidRepository bidRepository;
    private final PresidentRepository presidentRepository;
    private final PlayerRepository playerRepository;
    private final GameService gameService;
    private final PresidentService presidentService;

    public Response.AuctionStatus getCurrentAuctionStatus() {
        gameService.validatePhase(GamePhase.DRAFT_AUCTION);
        Player player = getCurrentAuctionPlayer();
        return buildAuctionStatus(player);
    }

    @Transactional
    public Response.AuctionStatus placeBid(Long playerId, Request.PlaceBid request) {
        gameService.validatePhase(GamePhase.DRAFT_AUCTION);

        Player player = getCurrentAuctionPlayer();
        if (!player.getId().equals(playerId)) {
            throw new ScoutException("Jogador em leilao agora e: " + player.getName() + " (ID: " + player.getId() + ")");
        }

        President president = presidentService.getPresident(request.getPresidentId());

        if (president.isTeamComplete()) {
            throw new ScoutException("Seu time ja esta completo (5 jogadores).");
        }
        if (request.getBidAmount() > president.getBudget()) {
            throw new ScoutException(String.format(
                "Saldo insuficiente. Saldo: R$ %.2f | Lance: R$ %.2f",
                president.getBudget(), request.getBidAmount()));
        }

        Optional<AuctionBid> currentHighest = bidRepository.findTopByPlayerIdOrderByBidAmountDesc(playerId);
        if (currentHighest.isPresent()) {
            if (request.getBidAmount() <= currentHighest.get().getBidAmount()) {
                throw new ScoutException(String.format(
                    "Lance deve ser maior que o atual: R$ %.2f", currentHighest.get().getBidAmount()));
            }
            if (currentHighest.get().getPresident().getId().equals(president.getId())) {
                throw new ScoutException("Voce ja e o maior licitante! Aguarde outros ou finalize o leilao.");
            }
        } else if (request.getBidAmount() < player.getValue()) {
            throw new ScoutException(String.format(
                "Lance minimo e o valor base do jogador: R$ %.2f", player.getValue()));
        }

        AuctionBid bid = AuctionBid.builder()
                .player(player)
                .president(president)
                .bidAmount(request.getBidAmount())
                .bidTime(LocalDateTime.now())
                .build();
        bidRepository.save(bid);

        log.info("💰 Lance de R$ {} por {} no jogador {}", request.getBidAmount(), president.getName(), player.getName());
        return buildAuctionStatus(player);
    }

    @Transactional
    public Response.AuctionStatus finalizeCurrentAuction() {
        gameService.validatePhase(GamePhase.DRAFT_AUCTION);
        Player player = getCurrentAuctionPlayer();

        Optional<AuctionBid> winnerBid = bidRepository.findTopByPlayerIdOrderByBidAmountDesc(player.getId());

        if (winnerBid.isPresent()) {
            AuctionBid winner = winnerBid.get();
            winner.setWinningBid(true);
            bidRepository.save(winner);

            President president = winner.getPresident();
            president.setBudget(president.getBudget() - winner.getBidAmount());
            president.setUsedBudget(president.getUsedBudget() + winner.getBidAmount());
            presidentRepository.save(president);

            player.setAvailable(false);
            player.setPresident(president);
            playerRepository.save(player);

            log.info("🏆 {} ganhou {} por R$ {}", president.getName(), player.getName(), winner.getBidAmount());
        } else {
            player.setAuctionPlayer(false);
            playerRepository.save(player);
            log.info("⚠️ Nenhum lance para {}. Vai para sorteio.", player.getName());
        }

        gameService.advanceAuctionToNextPlayer();
        return buildAuctionStatus(player);
    }

    private Player getCurrentAuctionPlayer() {
        GameState state = gameService.getGameState();
        List<Player> auctionPlayers = playerRepository.findByAuctionPlayerTrue().stream()
                .filter(Player::isAvailable)
                .collect(Collectors.toList());

        if (auctionPlayers.isEmpty() || state.getCurrentAuctionPlayerIndex() >= auctionPlayers.size()) {
            throw new ScoutException("Todos os jogadores do leilao ja foram processados.");
        }
        return auctionPlayers.get(state.getCurrentAuctionPlayerIndex());
    }

    private Response.AuctionStatus buildAuctionStatus(Player player) {
        List<AuctionBid> bids = bidRepository.findByPlayerIdOrderByBidAmountDesc(player.getId());
        Optional<AuctionBid> highest = bids.isEmpty() ? Optional.empty() : Optional.of(bids.get(0));

        List<Response.AuctionStatus.BidInfo> bidInfos = bids.stream()
                .map(b -> Response.AuctionStatus.BidInfo.builder()
                        .presidentName(b.getPresident().getName())
                        .bidAmount(b.getBidAmount())
                        .bidTime(b.getBidTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")))
                        .build())
                .collect(Collectors.toList());

        return Response.AuctionStatus.builder()
                .playerId(player.getId())
                .playerName(player.getName())
                .playerPosition(player.getPosition())
                .playerBaseValue(player.getValue())
                .currentHighestBid(highest.map(AuctionBid::getBidAmount).orElse(null))
                .currentLeader(highest.map(b -> b.getPresident().getName()).orElse("Nenhum lance"))
                .bids(bidInfos)
                .build();
    }
}