package com.balbuena.Scout.controller;

import com.balbuena.Scout.dto.Response;
import com.balbuena.Scout.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@Tag(name = "8. Relatorios")
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/standings")
    @Operation(summary = "Tabela de classificacao")
    public ResponseEntity<List<Response.Standing>> getStandings() {
        return ResponseEntity.ok(reportService.getStandings());
    }

    @GetMapping("/top-scorers")
    @Operation(summary = "Artilharia - top 10 goleadores")
    public ResponseEntity<List<Response.TopScorer>> getTopScorers() {
        return ResponseEntity.ok(reportService.getTopScorers());
    }

    @GetMapping("/best-attack")
    @Operation(summary = "Melhor ataque (mais gols marcados)")
    public ResponseEntity<Response.Standing> getBestAttack() {
        return ResponseEntity.ok(reportService.getBestAttack());
    }

    @GetMapping("/best-defense")
    @Operation(summary = "Melhor defesa (menos gols sofridos)")
    public ResponseEntity<Response.Standing> getBestDefense() {
        return ResponseEntity.ok(reportService.getBestDefense());
    }

    @GetMapping("/full-report")
    @Operation(summary = "Relatorio completo: campeao, tabela, artilharia, ataque e defesa")
    public ResponseEntity<Response.ChampionshipReport> getFullReport() {
        return ResponseEntity.ok(reportService.getFullReport());
    }
}   