package com.example.wc2026.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "matches")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "match_code")
    private String matchCode;

    @ManyToOne
    @JoinColumn(name = "home_team_id", nullable = false)
    private Team homeTeam; // Tham chiếu sang thực thể Đội nhà

    @ManyToOne
    @JoinColumn(name = "away_team_id", nullable = false)
    private Team awayTeam; // Tham chiếu sang thực thể Đội khách

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime; // Ngày giờ diễn ra trận đấu

    @Column(name = "home_score")
    private Integer homeScore = 0; // Tỷ số đội nhà, mặc định = 0

    @Column(name = "away_score")
    private Integer awayScore = 0; // Tỷ số đội khách, mặc định = 0

    @Column(nullable = false, length = 20)
    private String status; // Trạng thái: PENDING, ONGOING, FINISHED
}