package com.example.wc2026.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchResultRequest {

    @NotNull(message = "Tỷ số đội nhà không được để trống")
    @PositiveOrZero(message = "Tỷ số phải từ 0 trở lên")
    private Integer homeScore;

    @NotNull(message = "Tỷ số đội khách không được để trống")
    @PositiveOrZero(message = "Tỷ số phải từ 0 trở lên")
    private Integer awayScore;
}