package com.example.wc2026.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "bet_tickets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BetTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // fk_tickets_user nối sang bảng users

    @ManyToOne
    @JoinColumn(name = "match_id", nullable = false)
    private Match match; // fk_tickets_match nối sang bảng matches

    @ManyToOne
    @JoinColumn(name = "odds_id", nullable = false)
    private Odds odds; // fk_tickets_odds nối sang bảng odds dòng mới

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount; // Số tiền đặt cược

    @Column(name = "potential_win", nullable = false, precision = 15, scale = 2)
    private BigDecimal potentialWin; // Tiền thắng dự kiến (= amount * odds.rate)

    @Column(nullable = false, length = 20)
    private String status; // PENDING, WON, LOST

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt; // Thời gian lập vé cược
}