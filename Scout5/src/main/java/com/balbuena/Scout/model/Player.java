package com.balbuena.Scout.model;

import jakarta.persistence.*;
import lombok.*;

@Entity // estabelece que é uma entidade ( vira tabela no banco)
@Table(name = "players") //nome da tabela no banco
@Getter @Setter // gerar automatico get e set
@NoArgsConstructor //Hibernate precisa para instanciar objetos via reflexão
@AllArgsConstructor //gera construtor com todos os atributos da classe
@Builder //deixa mais legivel e evita erro na ordem dos parametros
public class Player {

    @Id //chave primaria da entidade no banco
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING) //para aumentar a segurança na criação, apenas 4 posicoes possiveis, se retirar, o banco cria como 0,1,2...
    @Column(nullable = false)
    private Position position;

    @Column(name = "market_value", nullable = false)
    private Double value;

    @Column(name = "is_auction_player")
    private boolean auctionPlayer;

    @Column(name = "is_available")
    @Builder.Default
    private boolean available = true;

    @Column(name = "goals_scored")
    @Builder.Default
    private int goalsScored = 0;

    @Column(name = "goals_conceded")
    @Builder.Default
    private int goalsConceded = 0;

    @Column(name = "matches_played")
    @Builder.Default
    private int matchesPlayed = 0;

    @ManyToOne(fetch = FetchType.LAZY)  //muitos jogadores pertencem a um presidente
    @JoinColumn(name = "president_id") //estabelece que cada jogador pertence a um presidente
    private President president;
}