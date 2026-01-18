package com.example.laptop.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.laptop.domain.Product;

@Repository
public interface ProductRepository extends
                JpaRepository<Product, Long>,
                JpaSpecificationExecutor<Product> {

        // ✅ Cách B cần để trả lời: "Acer có mấy máy?"
        long countByFactoryIgnoreCase(String factory);

        // ✅ Cách B cần để trả lời: "Máy rẻ nhất shop?"
        Optional<Product> findFirstByOrderByPriceAsc();

        // ✅ bạn đang có sẵn (giữ nguyên)
        List<Product> findTop5ByFactoryIgnoreCaseOrderByPriceAsc(String factory);

        // ✅ bạn đang có sẵn (giữ nguyên)
        List<Product> findTop5ByTargetIgnoreCaseOrderByPriceAsc(String target);

        // ✅ bạn đang có sẵn (giữ nguyên)
        List<Product> findTop5ByPriceLessThanEqualOrderByPriceAsc(BigDecimal maxPrice);
}
