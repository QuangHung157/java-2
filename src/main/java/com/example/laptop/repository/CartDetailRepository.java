package com.example.laptop.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.laptop.domain.CartDetail;

public interface CartDetailRepository extends JpaRepository<CartDetail, Long> {

    List<CartDetail> findByCartId(long cartId);

    Optional<CartDetail> findByCartIdAndProductId(long cartId, long productId);

    void deleteByCartId(long cartId);
}
