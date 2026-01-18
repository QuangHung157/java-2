package com.example.laptop.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // ✅ BigDecimal cho tiền
    @Column(precision = 18, scale = 2)
    private BigDecimal totalPrice = BigDecimal.ZERO;

    // ===== Receiver info =====
    private String receiverName;
    private String receiverAddress;
    private String receiverPhone;

    // PICKUP / DELIVERY
    private String shippingMethod;

    // NEW / PENDING / CONFIRMED / SHIPPING / DONE / CANCELED
    private String status;

    private LocalDateTime createdAt;

    // COD / VNPAY
    private String paymentMethod;

    // PENDING / PAID / FAILED
    private String paymentStatus;

    private String vnpTransactionNo;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetail> orderDetails;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();

        if (this.status == null || this.status.isBlank())
            this.status = "NEW";
        if (this.shippingMethod == null || this.shippingMethod.isBlank())
            this.shippingMethod = "DELIVERY";
        if (this.paymentMethod == null || this.paymentMethod.isBlank())
            this.paymentMethod = "COD";
        if (this.paymentStatus == null || this.paymentStatus.isBlank())
            this.paymentStatus = "PENDING";

        if (this.totalPrice == null)
            this.totalPrice = BigDecimal.ZERO;
    }

    // ===== GET/SET =====
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public String getShippingMethod() {
        return shippingMethod;
    }

    public void setShippingMethod(String shippingMethod) {
        this.shippingMethod = shippingMethod;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getVnpTransactionNo() {
        return vnpTransactionNo;
    }

    public void setVnpTransactionNo(String vnpTransactionNo) {
        this.vnpTransactionNo = vnpTransactionNo;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }
}
