package com.example.laptop.chatbot;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.laptop.domain.Order;
import com.example.laptop.domain.Product;
import com.example.laptop.repository.OrderRepository;
import com.example.laptop.repository.ProductRepository;

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

            case "countByFactory" -> {
                String factory = str(args.get("factory"));
                if (factory.isBlank())
                    yield Map.of("error", "FACTORY_REQUIRED");
                long count = productRepo.countByFactoryIgnoreCase(factory);
                yield Map.of("factory", factory, "count", count);
            }

            case "cheapestProduct" -> {
                Product p = productRepo.findFirstByOrderByPriceAsc().orElse(null);
                yield (p == null)
                        ? Map.of("found", false)
                        : Map.of(
                                "found", true,
                                "id", p.getId(),
                                "name", p.getName(),
                                "factory", p.getFactory(),
                                "target", p.getTarget(),
                                "price", p.getPrice().toString(),
                                "quantity", p.getQuantity());
            }

            case "cheapestByFactory" -> {
                String factory = str(args.get("factory"));
                if (factory.isBlank())
                    yield Map.of("error", "FACTORY_REQUIRED");
                List<Product> list = productRepo.findTop5ByFactoryIgnoreCaseOrderByPriceAsc(factory);
                if (list.isEmpty())
                    yield Map.of("factory", factory, "found", false);

                Product p = list.get(0);
                yield Map.of(
                        "factory", factory,
                        "found", true,
                        "id", p.getId(),
                        "name", p.getName(),
                        "price", p.getPrice().toString(),
                        "quantity", p.getQuantity());
            }

            case "recommendByTarget" -> {
                String target = str(args.get("target"));
                if (target.isBlank())
                    yield Map.of("error", "TARGET_REQUIRED");
                List<Product> list = productRepo.findTop5ByTargetIgnoreCaseOrderByPriceAsc(target);
                yield Map.of("target", target, "items", toProducts(list));
            }

            case "productsUnderPrice" -> {
                BigDecimal maxPrice = toBigDecimal(args.get("maxPrice"));
                if (maxPrice == null)
                    yield Map.of("error", "MAX_PRICE_REQUIRED");
                List<Product> list = productRepo.findTop5ByPriceLessThanEqualOrderByPriceAsc(maxPrice);
                yield Map.of("maxPrice", maxPrice.toString(), "items", toProducts(list));
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

    private List<Map<String, Object>> toProducts(List<Product> list) {
        return list.stream().map(p -> Map.<String, Object>of(
                "id", p.getId(),
                "name", p.getName(),
                "factory", p.getFactory(),
                "target", p.getTarget(),
                "price", p.getPrice().toString(),
                "quantity", p.getQuantity())).collect(Collectors.toList());
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
