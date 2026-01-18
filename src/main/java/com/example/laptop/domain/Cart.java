package com.example.laptop.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;

@Entity
@Table(name = "carts", uniqueConstraints = @UniqueConstraint(name = "uk_cart_user", columnNames = "user_id"))
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // Tổng quantity trong giỏ (badge trên header)
    @Min(0)
    @Column(nullable = false)
    private int sum = 0;

    // ✅ Hướng A: cart thuộc về user, không có sessionId nữa
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartDetail> cartDetails = new ArrayList<>();

    // ===== GET/SET =====
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<CartDetail> getCartDetails() {
        return cartDetails;
    }

    public void setCartDetails(List<CartDetail> cartDetails) {
        this.cartDetails = cartDetails;
    }
}
