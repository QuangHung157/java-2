package com.example.laptop.chatbot;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.laptop.domain.User;
import com.example.laptop.repository.UserRepository;

@RestController
@RequestMapping("/api")
public class ChatApiController {

    private final ChatOrchestrator orchestrator;
    private final UserRepository userRepo;
    private final SimpleRateLimiter limiter;

    public ChatApiController(ChatOrchestrator orchestrator, UserRepository userRepo, SimpleRateLimiter limiter) {
        this.orchestrator = orchestrator;
        this.userRepo = userRepo;
        this.limiter = limiter;
    }

    @PostMapping("/chat")
    public ResponseEntity<?> chat(@RequestBody ChatRequest req, Principal principal) {
        String msg = (req == null || req.getMessage() == null) ? "" : req.getMessage().trim();
        if (msg.isEmpty()) {
            return ResponseEntity.badRequest().body(new ChatResponse("Bạn nhập câu hỏi giúp mình nhé."));
        }

        String sessionId = (req.getSessionId() == null || req.getSessionId().isBlank())
                ? "anon"
                : req.getSessionId().trim();

        Long userId = null;
        String rateKey = "anon:" + sessionId;

        if (principal != null) {
            String email = principal.getName();
            User u = userRepo.findFirstByEmailOrderByIdAsc(email).orElse(null);
            if (u != null) {
                userId = u.getId();
                rateKey = "u:" + userId; // logged-in -> limit theo user
            }
        }

        // ✅ chặn spam: 8 req / 10s
        if (!limiter.allow(rateKey, 8, 10_000)) {
            return ResponseEntity.status(429)
                    .body(new ChatResponse("Bạn gửi hơi nhanh 😅 Chờ vài giây rồi thử lại nhé."));
        }

        String reply = orchestrator.chat(msg, userId, sessionId);
        return ResponseEntity.ok(new ChatResponse(reply));
    }

    public static class ChatRequest {
        private String message;
        private String sessionId;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getSessionId() {
            return sessionId;
        }

        public void setSessionId(String sessionId) {
            this.sessionId = sessionId;
        }
    }

    public static class ChatResponse {
        private String reply;

        public ChatResponse() {
        }

        public ChatResponse(String reply) {
            this.reply = reply;
        }

        public String getReply() {
            return reply;
        }

        public void setReply(String reply) {
            this.reply = reply;
        }
    }
}
