package com.example.laptop.payment.vnpay;

import jakarta.servlet.http.HttpServletRequest;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.TreeMap;

public class VnPayUtil {

    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isBlank()) {
            return ip.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    /**
     * Build query string + hashData from params (params MUST be sorted => TreeMap)
     * Return: queryString|||hashData
     * NOTE: Encode value consistently to avoid signature mismatch with spaces/VN
     * chars.
     */
    public static String buildQueryAndHashData(Map<String, String> sortedParams) {

        StringBuilder query = new StringBuilder();
        StringBuilder hashData = new StringBuilder();

        for (Map.Entry<String, String> entry : sortedParams.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            if (value == null || value.isEmpty())
                continue;

            String encodedValue = URLEncoder.encode(value, StandardCharsets.UTF_8);

            query.append(key)
                    .append("=")
                    .append(encodedValue)
                    .append("&");

            hashData.append(key)
                    .append("=")
                    .append(encodedValue)
                    .append("&");
        }

        if (query.length() > 0)
            query.deleteCharAt(query.length() - 1);
        if (hashData.length() > 0)
            hashData.deleteCharAt(hashData.length() - 1);

        return query + "|||" + hashData;
    }

    public static String hmacSHA512(String key, String data) {
        try {
            Mac hmac = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
            hmac.init(secretKey);

            byte[] bytes = hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder hash = new StringBuilder();
            for (byte b : bytes) {
                hash.append(String.format("%02x", b));
            }
            return hash.toString();
        } catch (Exception e) {
            throw new RuntimeException("Cannot create HMACSHA512", e);
        }
    }

    /**
     * Verify signature from VNPAY return / IPN
     */
    public static boolean verifySignature(Map<String, String> fields, String secureHash, String secretKey) {
        if (secureHash == null || secureHash.isBlank())
            return false;

        Map<String, String> sorted = new TreeMap<>(fields);

        String built = buildQueryAndHashData(sorted);
        String hashData = built.split("\\|\\|\\|")[1];

        String myHash = hmacSHA512(secretKey, hashData);
        return myHash.equalsIgnoreCase(secureHash);
    }
}
