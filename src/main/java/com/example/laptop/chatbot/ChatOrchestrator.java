package com.example.laptop.chatbot;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.example.laptop.chatbot.GeminiClient.GeminiResult;
import com.example.laptop.chatbot.GeminiClient.GeminiToolCall;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ChatOrchestrator {

    private final GeminiClient gemini;
    private final ToolExecutor toolExecutor;
    private final ObjectMapper mapper = new ObjectMapper();

    private static final Pattern FACTORY_PATTERN = Pattern.compile(
            "(acer|dell|asus|hp|lenovo|msi|gigabyte|lg|samsung)",
            Pattern.CASE_INSENSITIVE);

    private static final Pattern ORDER_PATTERN = Pattern.compile(
            "(đơn|order)\\s*#?(\\d+)",
            Pattern.CASE_INSENSITIVE);

    public ChatOrchestrator(GeminiClient gemini, ToolExecutor toolExecutor) {
        this.gemini = gemini;
        this.toolExecutor = toolExecutor;
    }

    public String chat(String userMessage, Long userId) {
        List<Map<String, Object>> tools = ToolDefinitions.tools();

        List<Map<String, Object>> messages = new ArrayList<>();
        messages.add(Map.of("role", "system", "content", systemPrompt()));
        messages.add(Map.of("role", "user", "content", userMessage));

        for (int step = 0; step < 3; step++) {
            GeminiResult res = gemini.generate(messages, tools);

            // Nếu Gemini trả “message lỗi” => fallback local
            if (looksLikeAiError(res.getContent())) {
                String fb = fallbackLocal(userMessage, userId);
                return fb != null ? fb : safeText(res.getContent(), "Hệ thống AI đang bận. Bạn thử lại sau nhé.");
            }

            // Nếu không có tool call -> trả final content (hoặc fallback)
            if (res.getToolCalls() == null || res.getToolCalls().isEmpty()) {
                String content = res.getContent();
                if (content == null || content.isBlank()) {
                    String fb = fallbackLocal(userMessage, userId);
                    return fb != null ? fb : "Mình chưa trả lời được, bạn thử lại nhé.";
                }
                return content;
            }

            // Append assistant message (để giữ hội thoại)
            messages.add(res.toAssistantMessageMap());

            // Execute tools
            for (GeminiToolCall tc : res.getToolCalls()) {
                Map<String, Object> args = parseArgs(tc.argumentsJson());
                Map<String, Object> toolResult = toolExecutor.execute(tc.name(), args, userId);

                // Gemini không bắt buộc tool_call_id, nhưng ta vẫn giữ để debug
                messages.add(Map.of(
                        "role", "tool",
                        "tool_call_id", tc.id(),
                        "content", toJson(toolResult)));
            }
        }

        String fb = fallbackLocal(userMessage, userId);
        return fb != null ? fb : "Mình xử lý hơi lâu. Bạn thử hỏi lại ngắn gọn hơn giúp mình nhé.";
    }

    private String systemPrompt() {
        return """
                Bạn là trợ lý tư vấn khách hàng cho LaptopShop (tiếng Việt).
                QUY TẮC:
                - Khi cần dữ liệu thật từ hệ thống (đếm hãng, máy rẻ nhất, đơn hàng...), PHẢI gọi tool.
                - Không bịa số liệu/giá/trạng thái.
                - Đơn hàng: nếu tool báo LOGIN_REQUIRED thì yêu cầu khách đăng nhập.
                - Hãng laptop lưu trong field Product.factory (Acer/Dell/Asus/HP/Lenovo...).
                - Mục đích lưu trong field Product.target (GAMING/OFFICE/STUDY/DESIGN...).
                Trả lời ngắn gọn, thân thiện, dễ hiểu.
                """;
    }

    private boolean looksLikeAiError(String content) {
        if (content == null)
            return false;
        String c = content.toLowerCase();
        return c.contains("ai đang bận")
                || c.contains("không kết nối")
                || c.contains("lỗi máy chủ")
                || c.contains("rate limit")
                || c.contains("quota")
                || c.contains("billing")
                || c.contains("try lại")
                || c.contains("gemini trả lỗi");
    }

    // ===== Fallback local: không cần Gemini vẫn trả lời được =====
    private String fallbackLocal(String msg, Long userId) {
        String lower = msg == null ? "" : msg.toLowerCase();

        if (lower.contains("rẻ nhất") || lower.contains("giá thấp nhất")) {
            Map<String, Object> r = toolExecutor.execute("cheapestProduct", Map.of(), userId);
            if (Boolean.TRUE.equals(r.get("found"))) {
                return "Máy rẻ nhất hiện tại là: " + r.get("name")
                        + " (" + r.get("factory") + "), giá " + r.get("price") + " VND.";
            }
            return "Hiện mình chưa thấy sản phẩm nào trong hệ thống.";
        }

        Matcher fm = FACTORY_PATTERN.matcher(msg);
        if ((lower.contains("mấy") || lower.contains("bao nhiêu") || lower.contains("số lượng")) && fm.find()) {
            String f = fm.group(1);
            Map<String, Object> r = toolExecutor.execute("countByFactory", Map.of("factory", f), userId);
            if (r.containsKey("count")) {
                return "Hãng " + f + " hiện có " + r.get("count") + " sản phẩm.";
            }
        }

        Matcher om = ORDER_PATTERN.matcher(msg);
        if (om.find()) {
            long oid = Long.parseLong(om.group(2));
            Map<String, Object> r = toolExecutor.execute("orderStatus", Map.of("orderId", oid), userId);

            if ("LOGIN_REQUIRED".equals(r.get("error"))) {
                return "Bạn đăng nhập giúp mình để mình tra trạng thái đơn hàng nhé.";
            }
            if (Boolean.FALSE.equals(r.get("found"))) {
                return "Mình không thấy đơn #" + oid + " (hoặc đơn không thuộc tài khoản của bạn).";
            }
            return "Đơn #" + r.get("orderId")
                    + " | Trạng thái: " + r.get("status")
                    + " | Thanh toán: " + r.get("paymentStatus")
                    + " | Tổng: " + r.get("totalPrice") + " VND.";
        }

        String target = null;
        if (lower.contains("gaming"))
            target = "GAMING";
        else if (lower.contains("văn phòng") || lower.contains("office"))
            target = "OFFICE";
        else if (lower.contains("học") || lower.contains("study") || lower.contains("sinh viên"))
            target = "STUDY";
        else if (lower.contains("đồ họa") || lower.contains("design"))
            target = "DESIGN";

        if (target != null) {
            Map<String, Object> r = toolExecutor.execute("recommendByTarget", Map.of("target", target), userId);
            Object items = r.get("items");
            return "Mình gợi ý một số máy cho mục đích " + target + ": "
                    + (items == null ? "hiện chưa có." : items.toString());
        }

        return null;
    }

    private String safeText(String s, String fallback) {
        if (s == null || s.isBlank())
            return fallback;
        return s;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> parseArgs(String argsJson) {
        if (argsJson == null || argsJson.isBlank())
            return Map.of();
        try {
            return mapper.readValue(argsJson, Map.class);
        } catch (Exception e) {
            return Map.of();
        }
    }

    private String toJson(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            return String.valueOf(obj);
        }
    }
}
