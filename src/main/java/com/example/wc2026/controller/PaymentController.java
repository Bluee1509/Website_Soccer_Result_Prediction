package com.example.wc2026.controller;

import com.example.wc2026.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.Collections;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin(origins = "*")
public class PaymentController {

    @Autowired
    private UserService userService;

    @PostMapping("/mock-deposit")
    public ResponseEntity<?> mockDeposit(
            @RequestParam("amount") long amount,
            Principal principal) {

        if (principal == null) {
            return ResponseEntity.status(401).body(Collections.singletonMap("message", "Vui lòng đăng nhập!"));
        }

        try {
            String username = principal.getName();
            BigDecimal depositAmount = new BigDecimal(amount);

            if (amount <= 0) {
                throw new RuntimeException("Số tiền nạp phải lớn hơn 0!");
            }

            // Gọi service xử lý cộng tiền và ghi log giao dịch
            userService.addBalance(username, depositAmount);

            return ResponseEntity.ok(Collections.singletonMap("message", "Nạp tiền thử nghiệm thành công!"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", e.getMessage()));
        }
    }
}