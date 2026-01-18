package com.example.laptop.payment.vnpay;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class VnPayService {

    @Value("${vnpay.tmnCode}")
    private String tmnCode;

    @Value("${vnpay.hashSecret}")
    private String hashSecret;

    @Value("${vnpay.payUrl}")
    private String payUrl; // mock gateway

    @Value("${vnpay.returnUrl}")
    private String returnUrl;

    @Value("${vnpay.ipnUrl}")
    private String ipnUrl;

    public String createPaymentUrl(long orderId, long amountVnd, String clientIp) {

        long vnpAmount = amountVnd * 100;

        TimeZone tz = TimeZone.getTimeZone("Asia/Ho_Chi_Minh");
        Calendar cal = Calendar.getInstance(tz);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        formatter.setTimeZone(tz);

        String vnpCreateDate = formatter.format(cal.getTime());
        cal.add(Calendar.MINUTE, 15);
        String vnpExpireDate = formatter.format(cal.getTime());

        Map<String, String> vnpParams = new TreeMap<>();
        vnpParams.put("vnp_Version", "2.1.0");
        vnpParams.put("vnp_Command", "pay");
        vnpParams.put("vnp_TmnCode", tmnCode);
        vnpParams.put("vnp_Amount", String.valueOf(vnpAmount));
        vnpParams.put("vnp_CurrCode", "VND");
        vnpParams.put("vnp_TxnRef", String.valueOf(orderId));
        vnpParams.put("vnp_OrderInfo", "Thanh toan don hang: " + orderId);
        vnpParams.put("vnp_OrderType", "other");
        vnpParams.put("vnp_Locale", "vn");

        vnpParams.put("vnp_ReturnUrl", returnUrl);
        vnpParams.put("vnp_IpnUrl", ipnUrl); // ✅ giống production

        vnpParams.put("vnp_IpAddr", clientIp);
        vnpParams.put("vnp_CreateDate", vnpCreateDate);
        vnpParams.put("vnp_ExpireDate", vnpExpireDate);

        String built = VnPayUtil.buildQueryAndHashData(vnpParams);
        String[] parts = built.split("\\|\\|\\|");
        String queryString = parts[0];
        String hashData = parts[1];

        String secureHash = VnPayUtil.hmacSHA512(hashSecret, hashData);

        return payUrl + "?" + queryString + "&vnp_SecureHash=" + secureHash;
    }

    public long toVndAmount(BigDecimal amount) {
        if (amount == null)
            return 0L;
        return amount.setScale(0, RoundingMode.HALF_UP).longValue();
    }
}
