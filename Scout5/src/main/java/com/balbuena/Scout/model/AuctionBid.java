package com.balbuena.Scout.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "auction_bids")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class AuctionBid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @ManyToOne
    @JoinColumn(name = "president_id", nullable = false)
    private President president;

    @Column(name = "bid_amount", nullable = false)
    private Double bidAmount;

    @Column(name = "bid_time")
    private LocalDateTime bidTime;

    @Builder.Default
    @Column(name = "winning_bid")
    private boolean winningBid = false;
}