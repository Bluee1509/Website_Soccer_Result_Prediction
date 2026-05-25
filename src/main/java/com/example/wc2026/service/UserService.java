package com.example.wc2026.service;

import com.example.wc2026.dto.LoginRequest;
import com.example.wc2026.dto.LoginResponse;
import com.example.wc2026.dto.UserRequest;
import com.example.wc2026.model.User;
import com.example.wc2026.repository.UserRepository;
import com.example.wc2026.security.JwtTokenProvider;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;


    private String normalizePhoneNumber(String phone) {
        String normalized = phone.trim();
        if (normalized.startsWith("+84")) {
            normalized = "0" + normalized.substring(3);
        } else if (normalized.startsWith("84") && normalized.length() > 10) {
            normalized = "0" + normalized.substring(2);
        }
        return normalized;
    }

    public User registerUser(UserRequest request) {

        String rawPhone = normalizePhoneNumber(request.getUsername());


        if (userRepository.existsByUsername(rawPhone)) {
            throw new RuntimeException("Tài khoản này đã tồn tại trong hệ thống!");
        }


        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email này đã được sử dụng!");
        }


        String encodedPassword = passwordEncoder.encode(request.getPassword());
        
        User user = User.builder()
                .username(rawPhone)
                .password(encodedPassword)
                .email(request.getEmail())
                .balance(BigDecimal.ZERO)
                .role("USER")
                .build();

        return userRepository.save(user);
    }


    public LoginResponse login(LoginRequest request) {

        String rawPhone = normalizePhoneNumber(request.getUsername());


        Optional<User> userOpt = userRepository.findByUsername(rawPhone);
        if (userOpt.isEmpty()) {
            return LoginResponse.error("Số điện thoại hoặc mật khẩu không chính xác!");
        }

        User user = userOpt.get();


        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return LoginResponse.error("Số điện thoại hoặc mật khẩu không chính xác!");
        }


        String token = jwtTokenProvider.generateToken(user.getUsername());


        return LoginResponse.success(user, token);
    }
    public List<User> getLeaderboard() {

        return userRepository.findTop10ByOrderByBalanceDesc();
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng: " + username));
    }

    @Transactional
    public void changePassword(String username, String oldPassword, String newPassword) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản!"));


        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("Mật khẩu hiện tại không chính xác!");
        }


        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}