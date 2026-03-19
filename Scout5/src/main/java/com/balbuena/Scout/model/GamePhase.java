package com.balbuena.Scout.model;

public enum GamePhase {
    REGISTRATION,       // Cadastro de presidentes
    DRAFT_AUCTION,      // Leilao dos 5 jogadores especiais
    DRAFT_LOTTERY,      // Sorteio dos demais jogadores
    CHAMPIONSHIP,       // Campeonato em andamento
    TRANSFER_WINDOW,    // Janela de transferencias (meio do campeonato)
    FINISHED            // Campeonato encerrado
}