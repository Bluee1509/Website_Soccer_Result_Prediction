package com.example.wc2026.dto;

import com.example.wc2026.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * LoginResponse DTO - Phản hồi đăng nhập
 * Trả về token, thông tin user, và các chi tiết khác
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {
    private String token; // JWT Token
    private String type; // "Bearer"
    private Long userId;
    private String username; // Số điện thoại
    private String email;
    private String role;
    private String message;

    /**
     * Factory method để tạo LoginResponse từ User và Token
     */
    public static LoginResponse success(User user, String token) {
        return LoginResponse.builder()
                .token(token)
                .type("Bearer")
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .message("Đăng nhập thành công")
                .build();
    }

    /**
     * Factory method để tạo LoginResponse cho lỗi
     */
    public static LoginResponse error(String message) {
        return LoginResponse.builder()
                .message(message)
                .type("Bearer")
                .build();
    }
}

