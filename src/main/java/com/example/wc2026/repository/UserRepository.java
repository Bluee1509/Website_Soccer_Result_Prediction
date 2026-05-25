package com.example.wc2026.repository;

import com.example.wc2026.model.User;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Hai hàm này dùng để kiểm tra xem tài khoản hoặc email đã có ai đăng ký chưa
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    // Tìm user theo username (số điện thoại)
    Optional<User> findByUsername(String username);

    // === THÊM MỚI: Lock theo username để đặt cược an toàn ===
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT u FROM User u WHERE u.username = :username")
    Optional<User> findByUsernameForUpdate(@Param("username") String username);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT u FROM User u WHERE u.id = :id")
    Optional<User> findByIdForUpdate(@Param("id") Long id);

    List<User> findTop10ByOrderByBalanceDesc();
}