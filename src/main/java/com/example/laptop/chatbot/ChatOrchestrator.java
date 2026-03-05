package com.example.laptop.chatbot;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

@Service
public class ChatOrchestrator {

    private final ToolExecutor toolExecutor;
    private final ChatSessionStore sessionStore;

    // giá: "15 triệu", "15tr", "15000000"
    private static final Pattern MONEY_TR = Pattern.compile("(\\d+(?:[\\.,]\\d+)?)\\s*(tr|triệu)",
            Pattern.CASE_INSENSITIVE);
    private static final Pattern MONEY_VND = Pattern.compile("(\\d{7,})");

    public ChatOrchestrator(ToolExecutor toolExecutor, ChatSessionStore sessionStore) {
        this.toolExecutor = toolExecutor;
        this.sessionStore = sessionStore;
    }

    public String chat(String userMessage, Long userId, String sessionId) {
        String msg = userMessage == null ? "" : userMessage.trim();
        if (msg.isBlank())
            return "Bạn nhập câu hỏi giúp mình nhé.";

        String lower = msg.toLowerCase();
        ChatSessionStore.State st = sessionStore.get(sessionId);

        // 0) reset
        if (containsAny(lower, "làm lại", "reset", "bắt đầu lại")) {
            sessionStore.clear(sessionId);
            return "Ok bạn. Mình làm lại từ đầu nhé.\nBạn muốn mua laptop để học tập, văn phòng, gaming hay đồ họa ạ?";
        }

        // 1) chào hỏi - kiểu nhân viên tư vấn
        if (isGreeting(lower)) {
            return "Chào bạn 👋 Mình là trợ lý tư vấn LaptopShop.\n"
                    + "Bạn đang cần tư vấn mua laptop cho mục đích nào: học tập, văn phòng, gaming hay đồ họa ạ?";
        }

        // 2) rẻ nhất / đắt nhất -> trả ngay
        if (containsAny(lower, "rẻ nhất", "thấp nhất")) {
            Map<String, Object> r = toolExecutor.execute("cheapestProduct", Map.of(), userId);
            return formatSingle("Laptop rẻ nhất hiện tại:", r)
                    + "\nBạn mua máy để học tập, văn phòng, gaming hay đồ họa ạ?";
        }
        if (containsAny(lower, "đắt nhất", "cao nhất")) {
            Map<String, Object> r = toolExecutor.execute("mostExpensiveProduct", Map.of(), userId);
            return formatSingle("Laptop đắt nhất hiện tại:", r)
                    + "\nBạn định dùng cho gaming hay đồ họa để mình gợi ý đúng hơn ạ?";
        }

        // 3) cập nhật state từ câu tự nhiên
        String detectedTarget = detectTarget(lower);
        if (detectedTarget != null)
            st.target = detectedTarget;

        String factory = detectFactory(lower);
        if (factory != null)
            st.factory = factory;

        Long money = parseMoneyToVnd(lower);
        if (money != null)
            st.maxPriceVnd = money;

        // 4) user nói muốn mua laptop chung chung
        if (containsAny(lower, "mua laptop", "mua máy", "tôi muốn mua", "tư vấn laptop", "tư vấn", "mua")) {
            if (st.target == null) {
                return "Dạ vâng 😊 Bạn muốn mua laptop để học tập, văn phòng, gaming hay đồ họa ạ?";
            }
            // đã có mục đích -> gợi ý nhanh 3 máy trước
            String quick = recommendQuick(st, userId);
            if (st.maxPriceVnd == null) {
                return quick + "\nBạn muốn tầm giá bao nhiêu triệu để mình lọc chính xác hơn ạ?";
            }
            // có giá rồi thì xuống phần lọc theo giá
        }

        // 5) user vừa nói mục đích (học tập/gaming/đồ họa) nhưng chưa nói giá
        if (detectedTarget != null && st.maxPriceVnd == null) {
            String quick = recommendQuick(st, userId);
            return quick + "\nBạn muốn tầm giá bao nhiêu triệu để mình lọc đúng ngân sách ạ?";
        }

        // 6) có mục đích + ngân sách -> lọc đúng và trả 5 máy
        if (st.target != null && st.maxPriceVnd != null) {
            Map<String, Object> r = toolExecutor.execute("searchProducts", Map.of(
                    "target", st.target,
                    "factory", st.factory == null ? "" : st.factory,
                    "maxPrice", st.maxPriceVnd,
                    "sort", "price_asc",
                    "limit", 5), userId);

            String title = "Gợi ý laptop cho " + prettyTarget(st.target) + " dưới " + formatMoney(st.maxPriceVnd)
                    + (st.factory != null ? (" (ưu tiên hãng " + st.factory + ")") : "")
                    + ":";
            return formatList(title, r) + "\nBạn thích máy mỏng nhẹ hay hiệu năng hơn ạ?";
        }

        // 7) fallback thân thiện
        return "Bạn muốn xem laptop theo rẻ nhất, đắt nhất, hay theo mục đích (học tập/văn phòng/gaming/đồ họa) ạ?";
    }

    // ===== Quick recommend: trả 3 máy theo mục đích (không cần ngân sách) =====
    private String recommendQuick(ChatSessionStore.State st, Long userId) {
        Map<String, Object> r = toolExecutor.execute("searchProducts", Map.of(
                "target", st.target,
                "factory", st.factory == null ? "" : st.factory,
                "sort", "price_asc",
                "limit", 3), userId);

        String title = "Một số laptop phù hợp cho " + prettyTarget(st.target)
                + (st.factory != null ? (" (hãng " + st.factory + ")") : "")
                + ":";
        return formatList(title, r);
    }

    private boolean isGreeting(String lower) {
        String s = lower.trim();
        return s.equals("hi") || s.equals("hello") || s.equals("xin chào")
                || s.equals("chào") || s.equals("hey");
    }

    private String detectTarget(String lower) {
        if (containsAny(lower, "học", "học tập", "sinh viên", "học sinh", "văn phòng", "office")) {
            return "SINHVIEN-VANPHONG";
        }
        if (containsAny(lower, "gaming", "chơi game")) {
            return "GAMING";
        }
        if (containsAny(lower, "đồ họa", "thiết kế", "design", "render", "photoshop")) {
            return "DOHOA"; // nếu DB bạn dùng DESIGN thì đổi thành "DESIGN"
        }
        return null;
    }

    private String detectFactory(String lower) {
        if (lower.contains("acer"))
            return "Acer";
        if (lower.contains("dell"))
            return "Dell";
        if (lower.contains("asus"))
            return "Asus";
        if (lower.contains("hp"))
            return "HP";
        if (lower.contains("lenovo"))
            return "Lenovo";
        if (lower.contains("msi"))
            return "MSI";
        if (lower.contains("apple") || lower.contains("macbook"))
            return "Apple";
        return null;
    }

    private Long parseMoneyToVnd(String lower) {
        Matcher m1 = MONEY_TR.matcher(lower);
        if (m1.find()) {
            String num = m1.group(1).replace(",", ".").trim();
            try {
                double v = Double.parseDouble(num);
                return (long) (v * 1_000_000);
            } catch (Exception ignore) {
            }
        }

        Matcher m2 = MONEY_VND.matcher(lower);
        if (m2.find()) {
            try {
                long v = Long.parseLong(m2.group(1));
                if (v >= 1_000_000)
                    return v;
            } catch (Exception ignore) {
            }
        }

        if (lower.contains("15tr"))
            return 15_000_000L;
        return null;
    }

    private boolean containsAny(String s, String... keys) {
        for (String k : keys)
            if (s.contains(k))
                return true;
        return false;
    }

    private String prettyTarget(String t) {
        if (t == null)
            return "";
        return switch (t.toUpperCase()) {
            case "SINHVIEN-VANPHONG" -> "học tập / văn phòng";
            case "GAMING" -> "gaming";
            case "DOHOA", "DESIGN" -> "đồ họa / thiết kế";
            default -> t;
        };
    }

    private String formatMoney(long vnd) {
        return String.format("%,d VND", vnd);
    }

    @SuppressWarnings("unchecked")
    private String formatList(String title, Map<String, Object> toolResult) {
        Object itemsObj = toolResult.get("items");
        if (!(itemsObj instanceof List<?> list) || list.isEmpty()) {
            return title + "\nHiện chưa có sản phẩm phù hợp.";
        }

        StringBuilder sb = new StringBuilder(title).append("\n");
        int i = 1;
        for (Object it : list) {
            if (!(it instanceof Map))
                continue;
            Map<String, Object> p = (Map<String, Object>) it;

            sb.append(i++).append(") ")
                    .append(val(p.get("name"))).append(" | ")
                    .append(val(p.get("factory"))).append(" | ")
                    .append(val(p.get("price"))).append(" VND\n");

            if (i > 5)
                break;
        }
        return sb.toString().trim();
    }

    @SuppressWarnings("unchecked")
    private String formatSingle(String title, Map<String, Object> toolResult) {
        if (!Boolean.TRUE.equals(toolResult.get("found"))) {
            return title + " Hiện chưa có dữ liệu.";
        }
        Object itemObj = toolResult.get("item");
        if (!(itemObj instanceof Map))
            return title + " Hiện chưa có dữ liệu.";

        Map<String, Object> p = (Map<String, Object>) itemObj;
        return title + " " + val(p.get("name")) + " (" + val(p.get("factory")) + "), giá " + val(p.get("price"))
                + " VND.";
    }

    private String val(Object o) {
        return o == null ? "" : o.toString();
    }
}
