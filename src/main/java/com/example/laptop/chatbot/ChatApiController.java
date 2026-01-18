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

    public ChatApiController(ChatOrchestrator orchestrator, UserRepository userRepo) {
        this.orchestrator = orchestrator;
        this.userRepo = userRepo;
    }

    // Frontend POST JSON: { "message": "..." }
    @PostMapping("/chat")
    public ResponseEntity<?> chat(@RequestBody ChatRequest req, Principal principal) {
        String msg = (req == null || req.getMessage() == null) ? "" : req.getMessage().trim();
        if (msg.isEmpty()) {
            return ResponseEntity.badRequest().body(new ChatResponse("Bạn nhập câu hỏi giúp mình nhé."));
        }

        Long userId = null;
        if (principal != null) {
            String email = principal.getName();
            User u = userRepo.findFirstByEmailOrderByIdAsc(email).orElse(null);
            if (u != null)
                userId = u.getId();
        }

        String reply = orchestrator.chat(msg, userId);
        return ResponseEntity.ok(new ChatResponse(reply));
    }

    public static class ChatRequest {
        private String message;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
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
