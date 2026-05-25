package com.example.wc2026.controller;

import com.example.wc2026.dto.ChangePasswordRequest;
import com.example.wc2026.dto.LoginRequest;
import com.example.wc2026.dto.LoginResponse;
import com.example.wc2026.dto.UserRequest;
import com.example.wc2026.model.User;
import com.example.wc2026.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserRequest request) {
        try {
            User registeredUser = userService.registerUser(request);
            return ResponseEntity.ok(registeredUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            LoginResponse response = userService.login(request);
            if (response.getToken() != null) {
                return ResponseEntity.ok(response);
            } else {

                return ResponseEntity.status(401).body(response);
            }
        } catch (RuntimeException e) {

            return ResponseEntity.status(401).body(LoginResponse.error(e.getMessage()));
        }
    }
    @GetMapping("/leaderboard")
    public ResponseEntity<List<User>> getLeaderboard() {

        return ResponseEntity.ok(userService    .getLeaderboard());
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/me/balance")
    public BigDecimal getBalance(Principal principal) {
        return userService.findByUsername(principal.getName()).getBalance();
    }
    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequest request, Principal principal) {

        userService.changePassword(principal.getName(), request.getOldPassword(), request.getNewPassword());
        return ResponseEntity.ok().body("{\"message\": \"Đổi mật khẩu thành công!\"}");
    }
}



