package com.example.laptop.chatbot;

import java.time.Duration;
import java.util.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Mono;

@Service
public class GeminiClient {

    private final WebClient webClient;
    private final ObjectMapper mapper = new ObjectMapper();

    @Value("${gemini.apiKey}")
    private String apiKey;

    @Value("${gemini.model}")
    private String model;

    public GeminiClient(@Value("${gemini.baseUrl}") String baseUrl) {
        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
    }

    /**
     * Call Gemini generateContent with function calling.
     * Endpoint: POST /v1beta/models/{model}:generateContent?key=... (AI Studio)
     */
    @SuppressWarnings("rawtypes")
    public GeminiResult generate(List<Map<String, Object>> messages, List<Map<String, Object>> tools) {
        if (tools == null)
            tools = List.of();

        Map<String, Object> body = Map.of(
                "contents", toGeminiContents(messages),
                "tools", toGeminiTools(tools),
                "generationConfig", Map.of("temperature", 0.3));

        int maxAttempt = 3;
        long[] backoffMs = new long[] { 400, 900 };

        for (int attempt = 1; attempt <= maxAttempt; attempt++) {
            final int attemptNo = attempt;

            try {
                Map res = webClient.post()
                        .uri(uriBuilder -> uriBuilder
                                .path("/v1beta/models/{model}:generateContent")
                                .queryParam("key", apiKey)
                                .build(model))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .bodyValue(body)
                        .retrieve()
                        .onStatus(s -> s.is4xxClientError() || s.is5xxServerError(),
                                resp -> resp.bodyToMono(String.class).defaultIfEmpty("")
                                        .flatMap(errBody -> {
                                            String msg = "Gemini HTTP " + resp.statusCode().value()
                                                    + " | attempt=" + attemptNo + "/" + maxAttempt
                                                    + " | body=" + errBody;
                                            System.out.println("=== GEMINI ERROR ===");
                                            System.out.println(msg);
                                            System.out.println("===================");
                                            return Mono.error(new RuntimeException(msg));
                                        }))
                        .bodyToMono(Map.class)
                        .timeout(Duration.ofSeconds(25))
                        .block();

                return GeminiResult.fromRaw(res);

            } catch (Exception ex) {
                String m = ex.getMessage() == null ? "" : ex.getMessage();

                // Không retry nếu key sai
                if (m.contains("Gemini HTTP 401") || m.contains("Gemini HTTP 403")) {
                    return GeminiResult.simple(
                            "Gemini API key không hợp lệ hoặc bị chặn quyền. Admin kiểm tra lại giúp mình nhé.");
                }

                // 429 -> retry nhẹ
                if (m.contains("Gemini HTTP 429")) {
                    if (attempt < maxAttempt) {
                        sleepQuiet(backoffMs[Math.min(attempt - 1, backoffMs.length - 1)]);
                        continue;
                    }
                    return GeminiResult.simple("AI đang quá tải hoặc hết hạn mức. Bạn thử lại sau ít phút nhé.");
                }

                // 5xx -> retry
                if (m.contains("Gemini HTTP 5")) {
                    if (attempt < maxAttempt) {
                        sleepQuiet(backoffMs[Math.min(attempt - 1, backoffMs.length - 1)]);
                        continue;
                    }
                    return GeminiResult.simple("Hệ thống AI đang bận hoặc lỗi máy chủ. Bạn thử lại sau ít phút nhé.");
                }

                // timeout/khác
                return GeminiResult.simple("Hệ thống AI đang bận hoặc lỗi kết nối. Bạn thử lại sau ít phút nhé.");
            }
        }

        return GeminiResult.simple("Không gọi được AI lúc này.");
    }

    private void sleepQuiet(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {
        }
    }

    // ===== Convert messages -> Gemini contents =====
    private List<Map<String, Object>> toGeminiContents(List<Map<String, Object>> messages) {
        // Gemini contents: [{role:"user"|"model", parts:[{text:"..."}]}]
        // Để đơn giản và ổn định: gộp system+history thành một prompt text.
        StringBuilder sb = new StringBuilder();
        for (Map<String, Object> m : messages) {
            String role = String.valueOf(m.get("role"));
            String content = m.get("content") == null ? "" : String.valueOf(m.get("content"));

            if ("system".equals(role))
                sb.append("[SYSTEM]\n").append(content).append("\n\n");
            else if ("user".equals(role))
                sb.append("[USER]\n").append(content).append("\n\n");
            else if ("assistant".equals(role))
                sb.append("[ASSISTANT]\n").append(content).append("\n\n");
            else if ("tool".equals(role))
                sb.append("[TOOL]\n").append(content).append("\n\n");
        }

        return List.of(Map.of(
                "role", "user",
                "parts", List.of(Map.of("text", sb.toString().trim()))));
    }

    // ===== Convert ToolDefinitions.tools() -> Gemini tools =====
    private List<Map<String, Object>> toGeminiTools(List<Map<String, Object>> openAiTools) {
        // openAiTools format: [{type:"function",
        // function:{name,description,parameters}}]
        // Gemini format: [{"functionDeclarations":[{name,description,parameters},
        // ...]}]
        List<Map<String, Object>> decls = new ArrayList<>();
        for (Map<String, Object> t : openAiTools) {
            Object fn = t.get("function");
            if (fn instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> fnMap = (Map<String, Object>) fn;
                decls.add(fnMap);
            }
        }
        return List.of(Map.of("functionDeclarations", decls));
    }

    // ===== DTO tool call =====
    public record GeminiToolCall(String id, String name, String argumentsJson) {
    }

    // ===== Result wrapper =====
    public static class GeminiResult {
        private String content;
        private List<GeminiToolCall> toolCalls;
        private Map<String, Object> assistantMessageMap;

        public String getContent() {
            return content;
        }

        public List<GeminiToolCall> getToolCalls() {
            return toolCalls;
        }

        public Map<String, Object> toAssistantMessageMap() {
            return assistantMessageMap;
        }

        public static GeminiResult simple(String text) {
            GeminiResult out = new GeminiResult();
            out.content = text;
            out.toolCalls = List.of();
            out.assistantMessageMap = Map.of("role", "assistant", "content", text);
            return out;
        }

        @SuppressWarnings({ "rawtypes", "unchecked" })
        public static GeminiResult fromRaw(Map raw) {
            GeminiResult out = new GeminiResult();
            if (raw == null)
                return out;

            // candidates[0].content.parts[]
            List candidates = (List) raw.get("candidates");
            if (candidates == null || candidates.isEmpty())
                return out;

            Map cand0 = (Map) candidates.get(0);
            Map content = (Map) cand0.get("content");
            if (content == null)
                return out;

            List parts = (List) content.get("parts");
            if (parts == null || parts.isEmpty())
                return out;

            StringBuilder text = new StringBuilder();
            List<GeminiToolCall> calls = new ArrayList<>();

            ObjectMapper mapper = new ObjectMapper();

            for (Object p : parts) {
                if (!(p instanceof Map pm))
                    continue;

                // text
                Object t = pm.get("text");
                if (t != null)
                    text.append(t.toString());

                // functionCall: { name: "...", args: { ... } }
                Object fc = pm.get("functionCall");
                if (fc instanceof Map fcm) {
                    String name = (String) fcm.get("name");
                    Object args = fcm.get("args");
                    String argsJson;
                    try {
                        argsJson = mapper.writeValueAsString(args == null ? Map.of() : args);
                    } catch (Exception e) {
                        argsJson = "{}";
                    }
                    calls.add(new GeminiToolCall(UUID.randomUUID().toString(), name, argsJson));
                }
            }

            out.content = text.toString().trim();
            out.toolCalls = calls;

            // assistant message map để orchestrator append lại
            out.assistantMessageMap = Map.of("role", "assistant", "content", out.content);

            return out;
        }
    }
}
