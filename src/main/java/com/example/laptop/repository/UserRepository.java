package com.example.laptop.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.laptop.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // ✅ check email exists (không crash)
    boolean existsByEmail(String email);

    // ✅ lấy 1 user đầu tiên theo email (tránh crash nếu DB lỡ có trùng)
    Optional<User> findFirstByEmailOrderByIdAsc(String email);

    User findByEmail(String userEmail);
}
