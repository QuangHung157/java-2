package com.example.laptop.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.laptop.domain.Order;
import com.example.laptop.domain.User;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // ✅ admin list
    List<Order> findAllByOrderByCreatedAtDesc();

    // ✅ lấy đơn theo user object (bạn đang dùng ở vài chỗ)
    List<Order> findByUser(User currentUser);

    // ✅ lấy top 5 đơn mới nhất theo userId
    List<Order> findTop5ByUserIdOrderByCreatedAtDesc(Long userId);

    // ✅ Cách B (production): tra cứu đơn theo id + userId để KHÔNG lộ đơn người
    // khác
    Optional<Order> findByIdAndUserId(Long id, Long userId);
}
