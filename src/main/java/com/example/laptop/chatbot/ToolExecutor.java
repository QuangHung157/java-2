package com.example.laptop.chatbot;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.laptop.domain.Order;
import com.example.laptop.domain.Product;
import com.example.laptop.repository.OrderRepository;
import com.example.laptop.repository.ProductRepository;

import jakarta.persistence.criteria.Predicate;

@Service
public class ToolExecutor {

    private final ProductRepository productRepo;
    private final OrderRepository orderRepo;

    public ToolExecutor(ProductRepository productRepo, OrderRepository orderRepo) {
        this.productRepo = productRepo;
        this.orderRepo = orderRepo;
    }

    public Map<String, Object> execute(String toolName, Map<String, Object> args, Long userId) {
        args = (args == null) ? Map.of() : args;

        return switch (toolName) {

            case "searchProducts" -> {
                String query = str(args.get("query"));
                String factory = str(args.get("factory"));
                String target = str(args.get("target"));
                BigDecimal minPrice = toBigDecimal(args.get("minPrice"));
                BigDecimal maxPrice = toBigDecimal(args.get("maxPrice"));
                String sort = str(args.get("sort"));
                int limit = toInt(args.get("limit"), 5);

                if (limit <= 0)
                    limit = 5;
                if (limit > 10)
                    limit = 10;

                Sort s = switch (sort) {
                    case "price_desc" -> Sort.by(Sort.Direction.DESC, "price");
                    case "sold_desc" -> Sort.by(Sort.Direction.DESC, "sold");
                    default -> Sort.by(Sort.Direction.ASC, "price"); // price_asc
                };

                Pageable pageable = PageRequest.of(0, limit, s);

                Specification<Product> spec = (root, cq, cb) -> {
                    List<Predicate> ps = new ArrayList<>();

                    if (!factory.isBlank()) {
                        ps.add(cb.equal(cb.lower(root.get("factory")), factory.toLowerCase()));
                    }
                    if (!target.isBlank()) {
                        ps.add(cb.equal(cb.lower(root.get("target")), target.toLowerCase()));
                    }
                    if (minPrice != null) {
                        ps.add(cb.greaterThanOrEqualTo(root.get("price"), minPrice));
                    }
                    if (maxPrice != null) {
                        ps.add(cb.lessThanOrEqualTo(root.get("price"), maxPrice));
                    }

                    if (!query.isBlank()) {
                        String like = "%" + query.toLowerCase() + "%";
                        Predicate pName = cb.like(cb.lower(root.get("name")), like);

                        // shortDesc optional
                        Predicate pShort;
                        try {
                            pShort = cb.like(cb.lower(root.get("shortDesc")), like);
                            ps.add(cb.or(pName, pShort));
                        } catch (Exception ignore) {
                            ps.add(pName);
                        }
                    }

                    return cb.and(ps.toArray(new Predicate[0]));
                };

                Page<Product> page = productRepo.findAll(spec, pageable);
                List<Map<String, Object>> items = page.getContent().stream()
                        .map(this::toProductSimple)
                        .collect(Collectors.toList());

                yield Map.of("items", items, "count", items.size());
            }

            case "cheapestProduct" -> {
                Product p = productRepo.findFirstByOrderByPriceAsc().orElse(null);
                yield p == null
                        ? Map.of("found", false)
                        : Map.of("found", true, "item", toProductSimple(p));
            }

            case "mostExpensiveProduct" -> {
                Product p = productRepo.findFirstByOrderByPriceDesc().orElse(null);
                yield p == null
                        ? Map.of("found", false)
                        : Map.of("found", true, "item", toProductSimple(p));
            }

            case "orderStatus" -> {
                if (userId == null)
                    yield Map.of("error", "LOGIN_REQUIRED");

                Long orderId = toLong(args.get("orderId"));
                if (orderId == null)
                    yield Map.of("error", "ORDER_ID_REQUIRED");

                Order o = orderRepo.findByIdAndUserId(orderId, userId).orElse(null);
                if (o == null)
                    yield Map.of("orderId", orderId, "found", false);

                yield Map.of(
                        "orderId", o.getId(),
                        "found", true,
                        "status", o.getStatus(),
                        "paymentMethod", o.getPaymentMethod(),
                        "paymentStatus", o.getPaymentStatus(),
                        "totalPrice", o.getTotalPrice().toString(),
                        "createdAt", o.getCreatedAt() == null ? null : o.getCreatedAt().toString());
            }

            default -> Map.of("error", "UNKNOWN_TOOL", "tool", toolName);
        };
    }

    private Map<String, Object> toProductSimple(Product p) {
        return Map.of(
                "id", p.getId(),
                "name", p.getName(),
                "factory", p.getFactory(),
                "target", p.getTarget(),
                "price", p.getPrice() == null ? "" : p.getPrice().toString(),
                "quantity", p.getQuantity(),
                "sold", p.getSold());
    }

    private String str(Object o) {
        return o == null ? "" : o.toString().trim();
    }

    private Long toLong(Object o) {
        if (o == null)
            return null;
        try {
            return Long.valueOf(o.toString());
        } catch (Exception e) {
            return null;
        }
    }

    private int toInt(Object o, int def) {
        if (o == null)
            return def;
        try {
            return Integer.parseInt(o.toString());
        } catch (Exception e) {
            return def;
        }
    }

    private BigDecimal toBigDecimal(Object o) {
        if (o == null)
            return null;
        try {
            return new BigDecimal(o.toString());
        } catch (Exception e) {
            return null;
        }
    }
}
