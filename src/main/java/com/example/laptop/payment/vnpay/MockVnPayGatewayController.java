package com.example.laptop.payment.vnpay;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.TreeMap;

@Controller
@RequestMapping("/mock-vnpay")
public class MockVnPayGatewayController {

    @Value("${vnpay.hashSecret}")
    private String hashSecret;

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/pay")
    public String payPage() {
        return "client/payment/mock-vnpay";
    }

    // ✅ nếu ai đó truy cập confirm bằng GET => không lỗi 405 nữa
    @GetMapping("/confirm")
    public String confirmGetFallback() {
        return "redirect:/";
    }

    // ✅ endpoint POST thật sự
    @PostMapping("/confirm")
    public String confirm(@RequestParam("result") String result,
            @RequestParam Map<String, String> params) {

        Map<String, String> fields = new TreeMap<>(params);

        String returnUrl = fields.get("vnp_ReturnUrl");
        String ipnUrl = fields.get("vnp_IpnUrl");

        // remove old signature fields
        fields.remove("vnp_SecureHash");
        fields.remove("vnp_SecureHashType");

        // set result like VNPAY
        if ("success".equalsIgnoreCase(result)) {
            fields.put("vnp_ResponseCode", "00");
            fields.put("vnp_TransactionStatus", "00");
        } else {
            fields.put("vnp_ResponseCode", "24"); // user cancel
            fields.put("vnp_TransactionStatus", "02");
        }

        fields.put("vnp_TransactionNo", String.valueOf(System.currentTimeMillis()));

        // sign again
        String built = VnPayUtil.buildQueryAndHashData(fields);
        String[] parts = built.split("\\|\\|\\|");
        String queryString = parts[0];
        String hashData = parts[1];

        String secureHash = VnPayUtil.hmacSHA512(hashSecret, hashData);

        // ✅ production-like: call IPN first (server-to-server)
        try {
            String ipnCall = ipnUrl + "?" + queryString + "&vnp_SecureHash=" + secureHash;
            restTemplate.getForObject(ipnCall, String.class);
        } catch (Exception ignored) {
        }

        // redirect to Return
        return "redirect:" + returnUrl + "?" + queryString + "&vnp_SecureHash=" + secureHash;
    }
}
