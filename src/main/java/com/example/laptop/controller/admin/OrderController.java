package com.example.laptop.controller.admin;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.laptop.domain.Order;
import com.example.laptop.domain.OrderDetail;
import com.example.laptop.service.OrderService;

@Controller
public class OrderController {

    private final OrderService orderService;

    // ✅ chỉ cho phép các status chuẩn
    private static final Set<String> ALLOWED_STATUS = Set.of(
            "NEW", "CONFIRMED", "SHIPPING", "DONE", "CANCELED");

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // ===== LIST =====
    @GetMapping("/admin/order")
    public String getOrderPage(Model model) {
        List<Order> orders = orderService.getAllOrders();
        model.addAttribute("orders", orders);
        return "admin/order/show";
    }

    // ===== DETAIL =====
    @GetMapping("/admin/order/{id}")
    public String getOrderDetailPage(Model model, @PathVariable long id) {
        Order order = orderService.getOrderById(id);
        List<OrderDetail> details = orderService.getOrderDetails(id);

        model.addAttribute("order", order);
        model.addAttribute("details", details);
        return "admin/order/detail";
    }

    // ===== UPDATE (GET) =====
    @GetMapping("/admin/order/update/{id}")
    public String getUpdateOrderPage(Model model, @PathVariable long id) {
        Order order = orderService.getOrderById(id);
        model.addAttribute("order", order);
        return "admin/order/update";
    }

    // ===== UPDATE (POST) =====
    @PostMapping("/admin/order/update")
    public String postUpdateOrderStatus(@RequestParam("id") long id,
            @RequestParam("status") String status) {

        String normalized = (status == null) ? "" : status.trim().toUpperCase();
        if (!ALLOWED_STATUS.contains(normalized)) {

            return "redirect:/admin/order/" + id;
        }

        // ✅ update theo business chuẩn (CONFIRMED trừ kho 1 lần, DONE => PAID)
        orderService.updateOrderStatus(id, normalized);

        return "redirect:/admin/order/" + id;
    }

    // ===== DELETE (GET) =====
    @GetMapping("/admin/order/delete/{id}")
    public String getDeleteOrderPage(Model model, @PathVariable long id) {
        model.addAttribute("id", id);
        return "admin/order/delete";
    }

    // ===== DELETE (POST) =====
    @PostMapping("/admin/order/delete")
    public String postDeleteOrder(@RequestParam("id") long id) {
        orderService.deleteOrder(id);
        return "redirect:/admin/order";
    }
}
