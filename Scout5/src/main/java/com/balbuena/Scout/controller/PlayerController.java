package com.balbuena.Scout.controller;

import com.balbuena.Scout.dto.Response;
import com.balbuena.Scout.service.PlayerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/players")
@RequiredArgsConstructor
@Tag(name = "3. Jogadores")
public class PlayerController {

    private final PlayerService playerService;

    @GetMapping
    @Operation(summary = "Catalogo completo dos 30 jogadores")
    public ResponseEntity<List<Response.Player>> findAll() {
        return ResponseEntity.ok(playerService.findAll());
    }

    @GetMapping("/available")
    @Operation(summary = "Jogadores disponiveis (sem time)")
    public ResponseEntity<List<Response.Player>> findAvailable() {
        return ResponseEntity.ok(playerService.findAvailable());
    }

    @GetMapping("/auction")
    @Operation(summary = "Os 5 jogadores de leilao")
    public ResponseEntity<List<Response.Player>> findAuctionPlayers() {
        return ResponseEntity.ok(playerService.findAuctionPlayers());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar jogador por ID")
    public ResponseEntity<Response.Player> findById(@PathVariable Long id) {
        return ResponseEntity.ok(playerService.findById(id));
    }
}