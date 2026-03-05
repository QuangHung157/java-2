package com.example.laptop.chatbot;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

@Component
public class SimpleRateLimiter {

    private static class Bucket {
        long windowStartMs;
        int count;
    }

    private final ConcurrentHashMap<String, Bucket> map = new ConcurrentHashMap<>();

    public boolean allow(String key, int limit, long windowMs) {
        long now = System.currentTimeMillis();
        Bucket b = map.computeIfAbsent(key, k -> {
            Bucket nb = new Bucket();
            nb.windowStartMs = now;
            nb.count = 0;
            return nb;
        });

        synchronized (b) {
            if (now - b.windowStartMs >= windowMs) {
                b.windowStartMs = now;
                b.count = 0;
            }
            b.count++;
            return b.count <= limit;
        }
    }
}
