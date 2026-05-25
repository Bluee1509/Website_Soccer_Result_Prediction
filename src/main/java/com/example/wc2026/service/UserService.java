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

    /**
     * Chuẩn hóa số điện thoại Việt Nam
     * Chuyển tất cả format về 0XXXXXXXXX
     * VD: +84912345678 → 0912345678
     *     84912345678 → 0912345678
     *     0912345678 → 0912345678
     */
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
        // Chuẩn hóa số điện thoại
        String rawPhone = normalizePhoneNumber(request.getUsername());

        // 1. Kiểm tra trùng tài khoản
        if (userRepository.existsByUsername(rawPhone)) {
            throw new RuntimeException("Tài khoản này đã tồn tại trong hệ thống!");
        }

        // 2. Kiểm tra trùng email
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email này đã được sử dụng!");
        }

        // 3. Tạo đối tượng User để lưu (encode password trước)
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        
        User user = User.builder()
                .username(rawPhone)
                .password(encodedPassword)  // Password đã hash
                .email(request.getEmail())
                .balance(BigDecimal.ZERO)
                .role("USER")
                .build();

        return userRepository.save(user);
    }

    /**
     * Đăng nhập user
     * Nhận vào LoginRequest (username + password)
     * Trả về LoginResponse chứa JWT Token
     */
    public LoginResponse login(LoginRequest request) {
        // Chuẩn hóa số điện thoại
        String rawPhone = normalizePhoneNumber(request.getUsername());

        // 1. Tìm user trong DB
        Optional<User> userOpt = userRepository.findByUsername(rawPhone);
        if (userOpt.isEmpty()) {
            return LoginResponse.error("Số điện thoại hoặc mật khẩu không chính xác!");
        }

        User user = userOpt.get();

        // 2. Kiểm tra password (so sánh plaintext password với encoded password)
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return LoginResponse.error("Số điện thoại hoặc mật khẩu không chính xác!");
        }

        // 3. Tạo JWT Token
        String token = jwtTokenProvider.generateToken(user.getUsername());

        // 4. Trả về response thành công kèm token
        return LoginResponse.success(user, token);
    }
    public List<User> getLeaderboard() {
        // Giả sử bác đã viết hàm findTop10ByOrderByBalanceDesc trong UserRepository
        return userRepository.findTop10ByOrderByBalanceDesc();
    }
    // Lấy toàn bộ danh sách user
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

        // 🌟 BẢO MẬT: Phải kiểm tra mật khẩu cũ có khớp với DB không
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("Mật khẩu hiện tại không chính xác!");
        }

        // Băm (Hash) mật khẩu mới và lưu lại
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}