package com.example.wc2026.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OddsRequest {

    @NotNull(message = "ID trận đấu không được để trống")
    private Long matchId;

    @NotBlank(message = "Loại kèo không được để trống (Ví dụ: 1X2, TAI_XIU)")
    private String oddType;

    @NotBlank(message = "Lựa chọn đặt cược không được để trống (Ví dụ: HOME, AWAY, DRAW)")
    private String choice;

    @NotNull(message = "Tỷ lệ cược không được để trống")
    @Positive(message = "Tỷ lệ cược phải là số dương")
    private BigDecimal rate;
}