package com.example.wc2026.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "teams")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name; // Tên đội bóng (Ví dụ: Argentina, Pháp, Bồ Đào Nha...)

    @Column(name = "logo_url")
    private String logoUrl; // Đường dẫn ảnh logo/quốc kỳ của đội bóng
}