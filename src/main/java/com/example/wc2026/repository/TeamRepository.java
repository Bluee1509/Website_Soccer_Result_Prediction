package com.example.wc2026.repository;

import com.example.wc2026.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    // Kiểm tra xem tên đội bóng đã tồn tại chưa để tránh tạo trùng
    boolean existsByName(String name);
}