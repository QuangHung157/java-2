package com.example.laptop.controller.admin;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.laptop.domain.Order;
import com.example.laptop.repository.OrderRepository;
import com.example.laptop.repository.ProductRepository;
import com.example.laptop.repository.UserRepository;

@Controller
public class DashboardController {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public DashboardController(UserRepository userRepository,
            ProductRepository productRepository,
            OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    @GetMapping("/admin")
    public String getDashboard(Model model) {

        long countUsers = userRepository.count();
        long countProducts = productRepository.count();
        long countOrders = orderRepository.count();

        LocalDateTime to = LocalDateTime.now();
        LocalDateTime from = to.minusDays(30);

        BigDecimal revenue30d = orderRepository.sumRevenueBetween(from, to);
        if (revenue30d == null)
            revenue30d = BigDecimal.ZERO;

        Pageable pageable = PageRequest.of(0, 5);
        List<Order> latestOrders = orderRepository.findLatestOrders(pageable);

        model.addAttribute("countUsers", countUsers);
        model.addAttribute("countProducts", countProducts);
        model.addAttribute("countOrders", countOrders);
        model.addAttribute("revenue30d", revenue30d);
        model.addAttribute("latestOrders", latestOrders);

        return "admin/dashboard/show";
    }
}
