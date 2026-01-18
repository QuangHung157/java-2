package com.example.laptop.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.laptop.domain.Cart;
import com.example.laptop.domain.CartDetail;
import com.example.laptop.domain.Order;
import com.example.laptop.domain.OrderDetail;
import com.example.laptop.domain.Product;
import com.example.laptop.domain.User;

import com.example.laptop.repository.CartDetailRepository;
import com.example.laptop.repository.CartRepository;
import com.example.laptop.repository.OrderDetailRepository;
import com.example.laptop.repository.OrderRepository;
import com.example.laptop.repository.ProductRepository;

import com.example.laptop.service.exception.OutOfStockException;

@Service
public class OrderService {

    private final CartRepository cartRepository;
    private final CartDetailRepository cartDetailRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;

    public OrderService(
            CartRepository cartRepository,
            CartDetailRepository cartDetailRepository,
            ProductRepository productRepository,
            OrderRepository orderRepository,
            OrderDetailRepository orderDetailRepository) {

        this.cartRepository = cartRepository;
        this.cartDetailRepository = cartDetailRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAllByOrderByCreatedAtDesc();
    }

    public Order getOrderById(long id) {
        return orderRepository.findById(id).orElse(null);
    }

    public List<OrderDetail> getOrderDetails(long orderId) {
        return orderDetailRepository.findByOrderId(orderId);
    }

    // =========================
    // APPLY STOCK (ONLY WHEN COD OR VNPAY PAID)
    // =========================
    @Transactional(rollbackFor = Exception.class)
    public void applyStockForOrder(long orderId) {
        List<OrderDetail> details = orderDetailRepository.findByOrderId(orderId);
        if (details == null || details.isEmpty())
            return;

        for (OrderDetail od : details) {
            Product product = productRepository.findById(od.getProduct().getId())
                    .orElseThrow(() -> new OutOfStockException("Product not found"));

            long qty = od.getQuantity();

            if (product.getQuantity() < qty) {
                throw new OutOfStockException(
                        "Product " + product.getName() +
                                " only has " + product.getQuantity() + " left.");
            }

            product.setQuantity(product.getQuantity() - qty);
            product.setSold(product.getSold() + qty);
            productRepository.save(product);
        }
    }

    // =========================
    // CREATE ORDER (USER)
    // =========================
    @Transactional(rollbackFor = Exception.class)
    public Order placeUserOrder(
            User user,
            String receiverName,
            String receiverAddress,
            String receiverPhone,
            String shippingMethod,
            String paymentMethod) {

        if (user == null)
            return null;

        Cart cart = cartRepository.findByUserId(user.getId()).orElse(null);
        if (cart == null)
            return null;

        List<CartDetail> cartDetails = cartDetailRepository.findByCartId(cart.getId());
        if (cartDetails == null || cartDetails.isEmpty())
            return null;

        boolean isVnpay = "VNPAY".equalsIgnoreCase(paymentMethod);

        Order order = new Order();
        order.setUser(user);
        order.setReceiverName(receiverName);
        order.setReceiverPhone(receiverPhone);

        if ("PICKUP".equalsIgnoreCase(shippingMethod)) {
            order.setShippingMethod("PICKUP");
            order.setReceiverAddress("");
        } else {
            order.setShippingMethod("DELIVERY");
            order.setReceiverAddress(receiverAddress);
        }

        order.setPaymentMethod(isVnpay ? "VNPAY" : "COD");
        order.setPaymentStatus("PENDING");
        order.setStatus(isVnpay ? "PENDING" : "NEW");

        order = orderRepository.save(order);

        // ✅ BigDecimal total
        BigDecimal totalPrice = BigDecimal.ZERO;

        for (CartDetail cd : cartDetails) {

            Product product = productRepository.findById(cd.getProduct().getId())
                    .orElseThrow(() -> new OutOfStockException("Product not found"));

            if (product.getQuantity() < cd.getQuantity()) {
                throw new OutOfStockException(
                        "Product " + product.getName() +
                                " only has " + product.getQuantity() + " left.");
            }

            OrderDetail od = new OrderDetail();
            od.setOrder(order);
            od.setProduct(product);

            // ✅ snapshot theo cart (cd.getPrice() là BigDecimal)
            od.setPrice(cd.getPrice());
            od.setQuantity(cd.getQuantity());
            orderDetailRepository.save(od);

            // ✅ total += price * qty
            totalPrice = totalPrice.add(
                    cd.getPrice().multiply(BigDecimal.valueOf(cd.getQuantity())));
        }

        order.setTotalPrice(totalPrice);
        orderRepository.save(order);

        // COD => trừ kho ngay
        if (!isVnpay) {
            applyStockForOrder(order.getId());
            order.setStatus("CONFIRMED");
            orderRepository.save(order);
        }

        // Clear cart
        cartDetailRepository.deleteAll(cartDetails);
        cart.setSum(0);
        cartRepository.save(cart);

        return order;
    }

    // =========================
    // UPDATE PAYMENT (VNPAY RETURN / IPN)
    // =========================
    @Transactional(rollbackFor = Exception.class)
    public void updatePayment(long orderId, String paymentStatus, String vnpTransactionNo) {

        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null)
            return;

        // ✅ idempotent
        if ("PAID".equalsIgnoreCase(order.getPaymentStatus())) {
            return;
        }

        String normalized = paymentStatus == null ? "" : paymentStatus.trim().toUpperCase();
        order.setPaymentStatus(normalized);

        if (vnpTransactionNo != null && !vnpTransactionNo.isBlank()) {
            order.setVnpTransactionNo(vnpTransactionNo);
        }

        if ("PAID".equalsIgnoreCase(normalized)) {
            applyStockForOrder(orderId);
            order.setStatus("CONFIRMED");
        } else {
            order.setStatus("CANCELED");
        }

        orderRepository.save(order);
    }

    // ADMIN
    @Transactional
    public void updateOrderStatus(long orderId, String status) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order != null) {
            order.setStatus(status);
            orderRepository.save(order);
        }
    }

    @Transactional
    public void deleteOrder(long orderId) {
        orderRepository.deleteById(orderId);
    }
}
