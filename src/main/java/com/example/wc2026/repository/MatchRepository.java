package com.example.wc2026.repository;

import com.example.wc2026.model.Match;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {

    // 🌟 THÊM DÒNG NÀY: Spring tự hiểu và phân trang dựa vào tham số Pageable
    Page<Match> findAll(Pageable pageable);
}