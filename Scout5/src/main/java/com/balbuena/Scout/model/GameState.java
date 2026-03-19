package com.balbuena.Scout.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "game_state")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class GameState {

    @Id
    private Long id = 1L; // Singleton

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private GamePhase phase = GamePhase.REGISTRATION;

    @Column(name = "current_round")
    @Builder.Default
    private int currentRound = 0;

    @Column(name = "current_auction_player_index")
    @Builder.Default
    private int currentAuctionPlayerIndex = 0;

    @Column(name = "total_rounds")
    @Builder.Default
    private int totalRounds = 6;
}