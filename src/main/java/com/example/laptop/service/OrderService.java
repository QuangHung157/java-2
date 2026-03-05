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

    // =========================
    // READ
    // =========================
    public List<Order> getAllOrders() {
        return orderRepository.findAllByOrderByCreatedAtDesc();
    }

    public Order getOrderById(long id) {
        return orderRepository.findById(id).orElse(null);
    }

    public List<OrderDetail> getOrderDetails(long orderId) {
        return orderDetailRepository.findByOrderId(orderId);
    }

    public List<Order> getOrdersByUserId(long userId) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    // =========================
    // CREATE ORDER (COD)
    // =========================
    @Transactional(rollbackFor = Exception.class)
    public Order placeUserOrderCOD(
            User user,
            String receiverName,
            String receiverAddress,
            String receiverPhone,
            String shippingMethod) {

        if (user == null)
            return null;

        Cart cart = cartRepository.findByUserId(user.getId()).orElse(null);
        if (cart == null)
            return null;

        List<CartDetail> cartDetails = cartDetailRepository.findByCartId(cart.getId());
        if (cartDetails == null || cartDetails.isEmpty())
            return null;

        // 1) tạo Order
        Order order = new Order();
        order.setUser(user);
        order.setReceiverName(receiverName);
        order.setReceiverPhone(receiverPhone);

        if ("PICKUP".equalsIgnoreCase(shippingMethod)) {
            order.setShippingMethod("PICKUP");
            order.setReceiverAddress("");
        } else {
            // DELIVERY: yêu cầu address
            if (receiverAddress == null || receiverAddress.isBlank()) {
                throw new IllegalArgumentException("Address is required for delivery.");
            }
            order.setShippingMethod("DELIVERY");
            order.setReceiverAddress(receiverAddress);
        }

        // COD thực tế
        order.setPaymentMethod("COD");
        order.setPaymentStatus("UNPAID");
        order.setStatus("NEW");
        order.setStockApplied(false);

        order = orderRepository.save(order);

        // 2) tạo OrderDetail + tính total
        BigDecimal totalPrice = BigDecimal.ZERO;

        for (CartDetail cd : cartDetails) {
            Product product = productRepository.findById(cd.getProduct().getId())
                    .orElseThrow(() -> new OutOfStockException("Product not found"));

            if (product.getQuantity() < cd.getQuantity()) {
                throw new OutOfStockException(
                        "Product " + product.getName() + " only has " + product.getQuantity() + " left.");
            }

            // ✅ snapshot price (ưu tiên lấy từ cartDetail nếu bạn đã tính sẵn)
            BigDecimal priceSnapshot = cd.getPrice();
            if (priceSnapshot == null)
                priceSnapshot = BigDecimal.ZERO;

            OrderDetail od = new OrderDetail();
            od.setOrder(order);
            od.setProduct(product);
            od.setPrice(priceSnapshot);
            od.setQuantity(cd.getQuantity());
            orderDetailRepository.save(od);

            totalPrice = totalPrice.add(priceSnapshot.multiply(BigDecimal.valueOf(cd.getQuantity())));
        }

        order.setTotalPrice(totalPrice);
        orderRepository.save(order);

        // 3) clear cart
        cartDetailRepository.deleteAll(cartDetails);
        cart.setSum(0);
        cartRepository.save(cart);

        return order;
    }

    // =========================
    // APPLY STOCK (trừ kho đúng 1 lần)
    // =========================
    @Transactional(rollbackFor = Exception.class)
    public void applyStockForOrder(long orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null)
            return;

        // ✅ idempotent: đã trừ kho rồi thì thôi
        if (order.isStockApplied())
            return;

        List<OrderDetail> details = orderDetailRepository.findByOrderId(orderId);
        if (details == null || details.isEmpty())
            return;

        for (OrderDetail od : details) {
            Product product = productRepository.findById(od.getProduct().getId())
                    .orElseThrow(() -> new OutOfStockException("Product not found"));

            long qty = od.getQuantity();
            if (product.getQuantity() < qty) {
                throw new OutOfStockException(
                        "Product " + product.getName() + " only has " + product.getQuantity() + " left.");
            }

            product.setQuantity(product.getQuantity() - qty);
            product.setSold(product.getSold() + (int) qty);
            productRepository.save(product);
        }

        order.setStockApplied(true);
        orderRepository.save(order);
    }

    // =========================
    // ADMIN: update status
    // CONFIRMED => trừ kho (COD)
    // =========================
    @Transactional(rollbackFor = Exception.class)
    public void updateOrderStatus(long orderId, String newStatus) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null)
            return;

        // ✅ Nếu chuyển sang CONFIRMED thì trừ kho trước (đúng đời + tránh bug)
        if ("CONFIRMED".equalsIgnoreCase(newStatus) && !order.isStockApplied()) {
            applyStockForOrder(orderId);
        }

        order.setStatus(newStatus);
        orderRepository.save(order);
    }

    @Transactional
    public void deleteOrder(long orderId) {
        orderRepository.deleteById(orderId);
    }
}
