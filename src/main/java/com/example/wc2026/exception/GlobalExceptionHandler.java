package com.example.wc2026.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice // Biến class thành bộ rình rập lỗi toàn cục của cả hệ thống
public class GlobalExceptionHandler {

    /**
     * 1. HỨNG LỖI VALIDATION (@Valid ở DTO)
     * Khi người dùng nhập sai định dạng SĐT, trống mật khẩu, trống tên đội bóng...
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        FieldError fieldError = ex.getBindingResult().getFieldError();
        String errorMessage = (fieldError != null)
                ? fieldError.getDefaultMessage()
                : "Dữ liệu gửi lên không hợp lệ!";

        Map<String, String> response = new HashMap<>();
        response.put("error", "Validation Error");
        response.put("message", errorMessage);

        return ResponseEntity.badRequest().body(response);
    }

    /**
     * 2. HỨNG LỖI LOGIC NGHIỆP VỤ (RuntimeException từ các Service ném lên)
     * Khi: Trùng tên đội, trùng tài khoản, ví tiền không đủ, Đội nhà trùng Đội khách...
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleBusinessExceptions(RuntimeException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Business Error");
        response.put("message", ex.getMessage()); // Móc chính xác câu chữ bác viết ở Service ra

        return ResponseEntity.badRequest().body(response);
    }
}