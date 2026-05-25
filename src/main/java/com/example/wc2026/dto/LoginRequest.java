package com.example.wc2026.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * LoginRequest DTO - Yêu cầu đăng nhập
 * Nhận vào tên đăng nhập (số điện thoại) và mật khẩu
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^(0|\\+84)(3|5|7|8|9)\\d{8}$",
            message = "Số điện thoại không hợp lệ. Vui lòng nhập lại (0912345678 hoặc +84912345678)")
    private String username; // Số điện thoại chuẩn hóa (0XXXXXXXXX)

    @NotBlank(message = "Mật khẩu không được để trống")
    private String password;
}

