package com.example.wc2026.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchRequest {

    @NotNull(message = "ID đội nhà không được để trống")
    private Long homeTeamId;

    @NotNull(message = "ID đội khách không được để trống")
    private Long awayTeamId;

    @NotNull(message = "Thời gian trận đấu không được để trống")
    private LocalDateTime startTime; // Định dạng chuẩn: YYYY-MM-DDTHH:mm:ss
}