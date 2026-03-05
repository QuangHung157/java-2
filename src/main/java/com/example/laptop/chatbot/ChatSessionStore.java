package com.example.laptop.chatbot;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

@Component
public class ChatSessionStore {

    public static class State {
        public String target; // SINHVIEN-VANPHONG, GAMING, DOHOA...
        public String factory; // Acer, Dell...
        public Long maxPriceVnd; // 15000000
        public Long minPriceVnd; // optional
    }

    private final ConcurrentHashMap<String, State> store = new ConcurrentHashMap<>();

    public State get(String sessionId) {
        if (sessionId == null || sessionId.isBlank())
            sessionId = "anon";
        return store.computeIfAbsent(sessionId, k -> new State());
    }

    public void clear(String sessionId) {
        if (sessionId == null || sessionId.isBlank())
            return;
        store.remove(sessionId);
    }
}
