package com.example.laptop.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.laptop.domain.Product;
import com.example.laptop.repository.ProductRepository;

import jakarta.persistence.criteria.Predicate;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /* ===================== BASIC CRUD ===================== */

    public List<Product> getAllProducts() {
        return this.productRepository.findAll();
    }

    public Product getProductById(long id) {
        return this.productRepository.findById(id).orElse(null);
    }

    public Product handleSaveProduct(Product product) {
        return this.productRepository.save(product);
    }

    public void deleteAProduct(long id) {
        this.productRepository.deleteById(id);
    }

    /* ===================== FILTER PRODUCTS ===================== */

    public Page<Product> filterProducts(
            List<String> factories,
            List<String> targets,
            List<String> prices,
            Pageable pageable) {

        Specification<Product> spec = (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            // filter factory
            if (factories != null && !factories.isEmpty()) {
                predicates.add(root.get("factory").in(factories));
            }

            // filter target
            if (targets != null && !targets.isEmpty()) {
                predicates.add(root.get("target").in(targets));
            }

            // price filter OR
            if (prices != null && !prices.isEmpty()) {
                List<Predicate> priceOr = new ArrayList<>();

                for (String p : prices) {
                    switch (p) {
                        case "UNDER_10" ->
                            priceOr.add(cb.lt(root.get("price"), 10_000_000));
                        case "FROM_10_15" ->
                            priceOr.add(cb.between(root.get("price"), 10_000_000, 15_000_000));
                        case "FROM_15_20" ->
                            priceOr.add(cb.between(root.get("price"), 15_000_000, 20_000_000));
                        case "OVER_20" ->
                            priceOr.add(cb.gt(root.get("price"), 20_000_000));
                        default -> {
                        }
                    }
                }

                if (!priceOr.isEmpty()) {
                    predicates.add(cb.or(priceOr.toArray(new Predicate[0])));
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return productRepository.findAll(spec, pageable);
    }
}
