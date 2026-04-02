package com.balbuena.Scout.service;

import com.balbuena.Scout.dto.Request;
import com.balbuena.Scout.dto.Response;
import com.balbuena.Scout.exception.ScoutException;
import com.balbuena.Scout.model.GamePhase;
import com.balbuena.Scout.model.Player;
import com.balbuena.Scout.model.President;
import com.balbuena.Scout.repository.PresidentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PresidentService {

    private final PresidentRepository presidentRepository;
    private final GameService gameService;

    @Transactional
    public Response.President create(Request.PresidentCreate request) {
        gameService.validatePhase(GamePhase.REGISTRATION);
        if (presidentRepository.existsByEmail(request.getEmail())) {
            throw new ScoutException("Email ja cadastrado: " + request.getEmail());
        }
        if (presidentRepository.existsByName(request.getName())) {
            throw new ScoutException("Nome ja cadastrado: " + request.getName());
        }
        President president = President.builder()
                .name(request.getName())
                .email(request.getEmail())
                .build();
        return toResponse(presidentRepository.save(president));
    }

    public List<Response.President> findAll() {
        return presidentRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public Response.President findById(Long id) {
        return toResponse(getPresident(id));
    }

    public President getPresident(Long id) {
        return presidentRepository.findById(id)
                .orElseThrow(() -> new ScoutException("President nao encontrado: ID " + id));
    }

    public Response.President toResponse(President p) {
        List<Response.Player> team = p.getTeam().stream()
                .map(this::playerToResponse)
                .collect(Collectors.toList());
        return Response.President.builder()
                .id(p.getId())
                .name(p.getName())
                .email(p.getEmail())
                .budget(p.getBudget())
                .usedBudget(p.getUsedBudget())
                .teamComplete(p.isTeamComplete())
                .hasGoalkeeper(p.hasGoalkeeper())
                .team(team)
                .points(p.getPoints())
                .wins(p.getWins())
                .draws(p.getDraws())
                .losses(p.getLosses())
                .goalsFor(p.getGoalsFor())
                .goalsAgainst(p.getGoalsAgainst())
                .goalDifference(p.getGoalDifference())
                .transferUsed(p.isTransferUsed())
                .build();
    }

    public Response.Player playerToResponse(Player p) {
        return Response.Player.builder()
                .id(p.getId())
                .name(p.getName())
                .position(p.getPosition())
                .value(p.getValue())
                .auctionPlayer(p.isAuctionPlayer())
                .available(p.isAvailable())
                .goalsScored(p.getGoalsScored())
                .goalsConceded(p.getGoalsConceded())
                .presidentName(p.getPresident() != null ? p.getPresident().getName() : null)
                .build();
    }
}