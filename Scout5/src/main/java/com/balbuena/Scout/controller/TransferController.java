package com.balbuena.Scout.controller;

import com.balbuena.Scout.dto.Request;
import com.balbuena.Scout.dto.Response;
import com.balbuena.Scout.service.TransferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transfers")
@RequiredArgsConstructor
@Tag(name = "7. Transferencias")
public class TransferController {

    private final TransferService transferService;

    @GetMapping("/available")
    @Operation(summary = "Jogadores disponiveis no mercado")
    public ResponseEntity<List<Response.Player>> getAvailable() {
        return ResponseEntity.ok(transferService.getAvailableForTransfer());
    }

    @PostMapping("/swap")
    @Operation(summary = "Trocar por jogador do mercado",
               description = "Body: { presidentId, playerOutId, playerInId }. Diferenca de valor e debitada/creditada.")
    public ResponseEntity<Response.President> swapWithMarket(@Valid @RequestBody Request.Transfer request) {
        return ResponseEntity.ok(transferService.swapWithAvailablePlayer(request));
    }

    @PostMapping("/negotiate")
    @Operation(summary = "Negociar com outro presidente",
               description = "Body: { presidentId, playerOutId, playerInId, targetPresidentId, offerAmount }.")
    public ResponseEntity<Response.President> negotiate(@Valid @RequestBody Request.Transfer request) {
        return ResponseEntity.ok(transferService.negotiateWithPresident(request));
    }
}