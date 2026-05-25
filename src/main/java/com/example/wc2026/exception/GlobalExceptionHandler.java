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


    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleBusinessExceptions(RuntimeException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Business Error");
        response.put("message", ex.getMessage());

        return ResponseEntity.badRequest().body(response);
    }
}