package com.example.laptop.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.laptop.domain.Order;
import com.example.laptop.domain.User;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // =========================
    // EXISTING (giữ nguyên của bạn)
    // =========================

    // admin list
    List<Order> findAllByOrderByCreatedAtDesc();

    // lấy đơn theo user object
    List<Order> findByUser(User currentUser);

    // top 5 đơn mới nhất theo userId
    List<Order> findTop5ByUserIdOrderByCreatedAtDesc(Long userId);

    Optional<Order> findByIdAndUserId(Long id, Long userId);

    List<Order> findByUserId(Long userId);

    List<Order> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<Order> findTop5ByOrderByCreatedAtDesc();

    long countByStatus(String status);

    // đếm đơn trong khoảng thời gian (vd 30 ngày)
    long countByCreatedAtBetween(LocalDateTime from, LocalDateTime to);

    // tổng doanh thu trong khoảng thời gian
    @Query("select coalesce(sum(o.totalPrice), 0) from Order o where o.createdAt between :from and :to")
    BigDecimal sumRevenueBetween(@Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to);

    // =========================
    // DASHBOARD SUPPORT (thêm nhẹ, không phá code cũ)
    // =========================
    @Query("select o from Order o order by o.createdAt desc")
    List<Order> findLatestOrders(Pageable pageable);
}
