package com.example.laptop.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.laptop.domain.Order;
import com.example.laptop.repository.OrderRepository;
import com.example.laptop.repository.ProductRepository;
import com.example.laptop.repository.UserRepository;

@Service
public class DashboardService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public DashboardService(UserRepository userRepository,
            ProductRepository productRepository,
            OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    public long countUsers() {
        return userRepository.count();
    }

    public long countProducts() {
        return productRepository.count();
    }

    public long countOrders() {
        return orderRepository.count();
    }

    public BigDecimal revenueBetween(LocalDateTime from, LocalDateTime to) {
        BigDecimal v = orderRepository.sumRevenueBetween(from, to);
        return v == null ? BigDecimal.ZERO : v;
    }

    public List<Order> latestOrders(int limit) {
        Pageable pageable = PageRequest.of(0, Math.max(1, limit));
        return orderRepository.findLatestOrders(pageable);
    }
}
