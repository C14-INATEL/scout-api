package com.balbuena.Scout.controller;

import com.balbuena.Scout.dto.Response;
import com.balbuena.Scout.service.DraftLotteryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lottery")
@RequiredArgsConstructor
@Tag(name = "5. Draft - Sorteio")
public class DraftLotteryController {

    private final DraftLotteryService lotteryService;

    @GetMapping("/available")
    @Operation(summary = "Jogadores disponiveis para sorteio")
    public ResponseEntity<List<Response.Player>> getAvailable() {
        return ResponseEntity.ok(lotteryService.getAvailableForLottery());
    }

    @PostMapping("/run")
    @Operation(summary = "Executar sorteio automatico",
               description = "Distribui jogadores restantes garantindo 1 goleiro por time e times completos (5 jogadores).")
    public ResponseEntity<Response.ApiMessage> runLottery() {
        return ResponseEntity.ok(Response.ApiMessage.of(lotteryService.runLottery()));
    }
}