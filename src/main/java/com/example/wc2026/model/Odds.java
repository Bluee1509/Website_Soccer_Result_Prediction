package com.example.wc2026.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "odds")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Odds {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;

    @Column(name = "odd_type", nullable = false, length = 50)
    private String oddType;

    @Column(nullable = false, length = 50)
    private String choice;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal rate;
}