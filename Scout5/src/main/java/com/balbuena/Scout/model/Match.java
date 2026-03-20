package com.balbuena.Scout.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "matches")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "round_number", nullable = false)
    private int roundNumber;

    @ManyToOne
    @JoinColumn(name = "home_president_id", nullable = false)
    private President homePresident;

    @ManyToOne
    @JoinColumn(name = "away_president_id", nullable = false)
    private President awayPresident;

    @Column(name = "home_goals")
    private Integer homeGoals;

    @Column(name = "away_goals")
    private Integer awayGoals;

    @Builder.Default
    private boolean played = false;
}