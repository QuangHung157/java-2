package com.example.laptop.chatbot;

import java.util.List;
import java.util.Map;

public class ToolDefinitions {

        public static List<Map<String, Object>> tools() {
                return List.of(
                                fn("searchProducts",
                                                "Tìm laptop theo tiêu chí: mục đích (target), hãng (factory), khoảng giá, từ khóa. Dùng cho tư vấn mua laptop.",
                                                obj(
                                                                Map.of(
                                                                                "query",
                                                                                Map.of("type", "string", "description",
                                                                                                "Từ khóa tự do, ví dụ: 'mỏng nhẹ', 'pin trâu'"),
                                                                                "factory",
                                                                                Map.of("type", "string", "description",
                                                                                                "Hãng: Acer/Dell/Asus/HP/Lenovo/MSI/Apple..."),
                                                                                "target",
                                                                                Map.of("type", "string", "description",
                                                                                                "Mục đích theo DB, ví dụ: SINHVIEN-VANPHONG, GAMING..."),
                                                                                "minPrice",
                                                                                Map.of("type", "number", "description",
                                                                                                "Giá tối thiểu (VND)"),
                                                                                "maxPrice",
                                                                                Map.of("type", "number", "description",
                                                                                                "Giá tối đa (VND)"),
                                                                                "sort",
                                                                                Map.of("type", "string", "description",
                                                                                                "price_asc|price_desc|sold_desc"),
                                                                                "limit",
                                                                                Map.of("type", "integer", "description",
                                                                                                "Số lượng trả về (1-10), default 5")),
                                                                List.of())),

                                fn("cheapestProduct",
                                                "Lấy sản phẩm rẻ nhất trong toàn shop.",
                                                obj(Map.of(), List.of())),

                                fn("mostExpensiveProduct",
                                                "Lấy sản phẩm đắt nhất trong toàn shop.",
                                                obj(Map.of(), List.of())),

                                fn("orderStatus",
                                                "Tra cứu đơn hàng theo orderId (chỉ cho user đang đăng nhập và đơn thuộc user).",
                                                obj(Map.of(
                                                                "orderId",
                                                                Map.of("type", "integer", "description",
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
                                "required", required);
        }
}
