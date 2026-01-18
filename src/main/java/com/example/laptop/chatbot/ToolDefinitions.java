package com.example.laptop.chatbot;

import java.util.List;
import java.util.Map;

public class ToolDefinitions {

        public static List<Map<String, Object>> tools() {
                return List.of(
                                fn("countByFactory",
                                                "Đếm số laptop theo hãng (field factory). Ví dụ: Acer, Dell, Asus, HP, Lenovo.",
                                                obj(
                                                                Map.of(
                                                                                "factory", Map.of(
                                                                                                "type", "string",
                                                                                                "description",
                                                                                                "Tên hãng, ví dụ: Acer")),
                                                                List.of("factory"))),

                                fn("cheapestProduct",
                                                "Lấy sản phẩm rẻ nhất trong toàn shop.",
                                                obj(Map.of(), List.of())),

                                fn("cheapestByFactory",
                                                "Lấy sản phẩm rẻ nhất theo hãng (factory).",
                                                obj(
                                                                Map.of(
                                                                                "factory", Map.of(
                                                                                                "type", "string",
                                                                                                "description",
                                                                                                "Tên hãng, ví dụ: Acer")),
                                                                List.of("factory"))),

                                fn("recommendByTarget",
                                                "Gợi ý laptop theo mục đích (field target) như GAMING/OFFICE/STUDY/DESIGN.",
                                                obj(
                                                                Map.of(
                                                                                "target", Map.of(
                                                                                                "type", "string",
                                                                                                "description",
                                                                                                "Mục đích, ví dụ: GAMING")),
                                                                List.of("target"))),

                                fn("productsUnderPrice",
                                                "Gợi ý laptop dưới hoặc bằng mức giá (VND).",
                                                obj(
                                                                Map.of(
                                                                                "maxPrice", Map.of(
                                                                                                "type", "number",
                                                                                                "description",
                                                                                                "Giá tối đa (VND), ví dụ: 20000000")),
                                                                List.of("maxPrice"))),

                                fn("orderStatus",
                                                "Tra cứu đơn hàng theo orderId (chỉ cho user đang đăng nhập và đơn thuộc user).",
                                                obj(
                                                                Map.of(
                                                                                "orderId", Map.of(
                                                                                                "type", "integer",
                                                                                                "description",
                                                                                                "Mã đơn hàng, ví dụ: 12")),
                                                                List.of("orderId"))));
        }

        private static Map<String, Object> fn(String name, String description, Map<String, Object> parameters) {
                return Map.of(
                                "type", "function",
                                "function", Map.of(
                                                "name", name,
                                                "description", description,
                                                "parameters", parameters));
        }

        private static Map<String, Object> obj(Map<String, Object> properties, List<String> required) {
                return Map.of(
                                "type", "object",
                                "properties", properties,
                                "required", required
                // ❌ bỏ additionalProperties
                );
        }

}
