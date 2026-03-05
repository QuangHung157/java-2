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

    @SuppressWarnings("rawtypes")
    public GeminiResult generate(List<Map<String, Object>> messages, List<Map<String, Object>> tools) {
        if (tools == null)
            tools = List.of();

        Map<String, Object> body = Map.of(
                "contents", toGeminiContents(messages),
                "tools", toGeminiTools(tools),
                "generationConfig", Map.of("temperature", 0.3));

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
                                        String msg = "Gemini HTTP " + resp.statusCode().value() + " | body=" + errBody;
                                        System.out.println("=== GEMINI ERROR ===");
                                        System.out.println(msg);
                                        System.out.println("===================");
                                        return Mono.error(new RuntimeException(msg));
                                    }))
                    .bodyToMono(Map.class)
                    .timeout(Duration.ofSeconds(20))
                    .block();

            return GeminiResult.fromRaw(res);

        } catch (Exception ex) {
            String m = ex.getMessage() == null ? "" : ex.getMessage();

            if (m.contains("Gemini HTTP 401") || m.contains("Gemini HTTP 403")) {
                return GeminiResult.simple("Gemini API key không hợp lệ hoặc bị chặn quyền.");
            }
            if (m.contains("Gemini HTTP 429")) {
                // ✅ không retry để khỏi treo request
                return GeminiResult.simple("AI đang quá tải hoặc hết hạn mức.");
            }
            if (m.contains("Gemini HTTP 5")) {
                return GeminiResult.simple("Hệ thống AI đang bận hoặc lỗi máy chủ.");
            }
            return GeminiResult.simple("Hệ thống AI đang bận hoặc lỗi kết nối.");
        }
    }

    private List<Map<String, Object>> toGeminiContents(List<Map<String, Object>> messages) {
        StringBuilder sb = new StringBuilder();
        for (Map<String, Object> m : messages) {
            String role = String.valueOf(m.get("role"));
            String content = m.get("content") == null ? "" : String.valueOf(m.get("content"));
            sb.append("[").append(role.toUpperCase()).append("]\n").append(content).append("\n\n");
        }
        return List.of(Map.of("role", "user", "parts", List.of(Map.of("text", sb.toString().trim()))));
    }

    private List<Map<String, Object>> toGeminiTools(List<Map<String, Object>> openAiTools) {
        List<Map<String, Object>> decls = new ArrayList<>();
        for (Map<String, Object> t : openAiTools) {
            Object fn = t.get("function");
            if (fn instanceof Map<?, ?> f) {
                @SuppressWarnings("unchecked")
                Map<String, Object> fnMap = (Map<String, Object>) f;
                decls.add(fnMap);
            }
        }
        return List.of(Map.of("functionDeclarations", decls));
    }

    public record GeminiToolCall(String id, String name, String argumentsJson) {
    }

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
            for (Object p : parts) {
                if (!(p instanceof Map pm))
                    continue;
                Object t = pm.get("text");
                if (t != null)
                    text.append(t.toString());
            }

            out.content = text.toString().trim();
            out.toolCalls = List.of();
            out.assistantMessageMap = Map.of("role", "assistant", "content", out.content);
            return out;
        }
    }
}
