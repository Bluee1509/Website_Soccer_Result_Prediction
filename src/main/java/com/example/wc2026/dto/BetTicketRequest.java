package com.example.wc2026.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import java.math.BigDecimal;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BetTicketRequest {


    @NotNull(message = "ID trận đấu không được để trống")
    private Long matchId;

    @NotNull(message = "ID dòng cược không được để trống")
    private Long oddsId;

    @NotNull(message = "Số tiền cược không được để trống")
    @Positive(message = "Số tiền cược phải lớn hơn 0")
    private BigDecimal amount;
}