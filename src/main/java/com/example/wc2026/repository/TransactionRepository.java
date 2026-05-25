package com.example.wc2026.repository;

import com.example.wc2026.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List; // ⚠️ Dòng này là bắt buộc để bác sử dụng List

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // Spring Data JPA sẽ tự động tạo truy vấn dựa trên tên phương thức này
    List<Transaction> findByUserIdOrderByCreatedAtDesc(Long userId);
    List<Transaction> findByUserUsernameOrderByCreatedAtDesc(String username);
}