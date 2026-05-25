package com.example.wc2026.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamRequest {

    @NotBlank(message = "Tên đội bóng không được để trống")
    private String name;

    private String logoUrl;
}