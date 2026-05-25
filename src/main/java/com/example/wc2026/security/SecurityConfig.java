package com.example.wc2026.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    // Đọc giá trị từ application.yml. Nếu không tìm thấy, mặc định là true (Bảo mật)
    @Value("${app.security.enabled:true}")
    private boolean securityEnabled;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        if (!securityEnabled) {
            // NẾU CÔNG TẮC = FALSE (Chế độ Test/Dev)
            http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
        } else {
            // NẾU CÔNG TẮC = TRUE (Chế độ Production thật)
            http.authorizeHttpRequests(auth -> auth
                            // 🌟 ĐÃ THÊM /favicon.ico VÀO ĐÂY ĐỂ XÓA LỖI ĐỎ CONSOLE
                            .requestMatchers("/", "/*.html", "/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/favicon.ico").permitAll()
                            .requestMatchers("/api/users/register", "/api/users/login").permitAll()

                            // Quyền ADMIN
                            .requestMatchers(HttpMethod.POST, "/api/teams").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.POST, "/api/matches").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.POST, "/api/odds").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.PUT, "/api/matches/**").hasRole("ADMIN")

                            // Quyền xem
                            .requestMatchers(HttpMethod.GET, "/api/teams").permitAll()
                            .requestMatchers(HttpMethod.GET, "/api/matches/**").permitAll()
                            .requestMatchers(HttpMethod.GET, "/api/odds/**").permitAll()

                            .requestMatchers("/api/users").hasRole("ADMIN")

                            // 🌟 KHAI BÁO ĐÍCH DANH: Mở đường cho API vé cược và giao dịch (Chỉ cần có Token hợp lệ)
                            .requestMatchers("/api/bets/**", "/api/transactions/**").authenticated()

                            .anyRequest().authenticated()
                    )
                    .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        }
        return http.build();
    }
}