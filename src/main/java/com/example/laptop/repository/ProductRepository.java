package com.example.laptop.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.laptop.domain.Product;

@Repository
public interface ProductRepository
                extends JpaRepository<Product, Long>,
                JpaSpecificationExecutor<Product> {

        Optional<Product> findFirstByOrderByPriceAsc();

        Optional<Product> findFirstByOrderByPriceDesc();

        // ✅ search theo tên (ShopController cần)
        Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);

        // ✅ dashboard (đếm sản phẩm sắp hết hàng)
        long countByQuantityLessThan(long quantity);
}
