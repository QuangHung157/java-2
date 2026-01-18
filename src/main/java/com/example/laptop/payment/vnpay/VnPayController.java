package com.example.laptop.payment.vnpay;

import com.example.laptop.domain.Order;
import com.example.laptop.service.OrderService;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/payment/vnpay")
public class VnPayController {

    private final VnPayService vnPayService;
    private final OrderService orderService;

    @Value("${vnpay.hashSecret}")
    private String hashSecret;

    public VnPayController(VnPayService vnPayService, OrderService orderService) {
        this.vnPayService = vnPayService;
        this.orderService = orderService;
    }

    @GetMapping("/create")
    public String create(@RequestParam("orderId") long orderId,
            HttpServletRequest request,
            RedirectAttributes ra) {

        Order order = orderService.getOrderById(orderId);
        if (order == null) {
            ra.addFlashAttribute("errorMessage", "Order not found.");
            return "redirect:/cart";
        }

        if (!"VNPAY".equalsIgnoreCase(order.getPaymentMethod())) {
            ra.addFlashAttribute("errorMessage", "This order is not VNPAY.");
            return "redirect:/thanks?id=" + orderId;
        }

        long amountVnd = vnPayService.toVndAmount(order.getTotalPrice());
        if (amountVnd <= 0) {
            ra.addFlashAttribute("errorMessage", "Invalid order amount.");
            return "redirect:/thanks?id=" + orderId;
        }

        String ip = VnPayUtil.getIpAddress(request);
        String paymentUrl = vnPayService.createPaymentUrl(orderId, amountVnd, ip);

        return "redirect:" + paymentUrl;
    }

    @GetMapping("/return")
    public String paymentReturn(HttpServletRequest request, RedirectAttributes ra) {

        Map<String, String> fields = extractParams(request);

        String secureHash = fields.get("vnp_SecureHash");
        String txnRef = fields.get("vnp_TxnRef");
        String responseCode = fields.get("vnp_ResponseCode");

        if (txnRef == null || txnRef.isBlank()) {
            ra.addFlashAttribute("errorMessage", "Missing order reference.");
            return "redirect:/cart";
        }

        long orderId;
        try {
            orderId = Long.parseLong(txnRef);
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", "Invalid order reference.");
            return "redirect:/cart";
        }

        Order order = orderService.getOrderById(orderId);
        if (order == null) {
            ra.addFlashAttribute("errorMessage", "Order not found.");
            return "redirect:/cart";
        }

        fields.remove("vnp_SecureHash");
        fields.remove("vnp_SecureHashType");

        boolean valid = VnPayUtil.verifySignature(fields, secureHash, hashSecret);
        if (!valid) {
            ra.addFlashAttribute("errorMessage", "Invalid signature.");
            return "redirect:/thanks?id=" + orderId;
        }

        // ✅ Return chỉ hiển thị, DB cập nhật bởi IPN
        if ("00".equals(responseCode)) {
            ra.addFlashAttribute("successMessage", "Payment success. Confirming...");
        } else {
            ra.addFlashAttribute("errorMessage", "Payment failed. Code: " + responseCode);
        }

        return "redirect:/thanks?id=" + orderId;
    }

    @GetMapping("/ipn")
    @ResponseBody
    public Map<String, String> ipn(HttpServletRequest request) {

        Map<String, String> fields = extractParams(request);

        String secureHash = fields.get("vnp_SecureHash");
        String txnRef = fields.get("vnp_TxnRef");
        String responseCode = fields.get("vnp_ResponseCode");
        String transactionNo = fields.get("vnp_TransactionNo");
        String amountStr = fields.get("vnp_Amount");

        Map<String, String> resp = new HashMap<>();

        if (txnRef == null || txnRef.isBlank()) {
            resp.put("RspCode", "01");
            resp.put("Message", "Missing order reference");
            return resp;
        }

        long orderId;
        try {
            orderId = Long.parseLong(txnRef);
        } catch (Exception e) {
            resp.put("RspCode", "01");
            resp.put("Message", "Invalid order reference");
            return resp;
        }

        Order order = orderService.getOrderById(orderId);
        if (order == null) {
            resp.put("RspCode", "01");
            resp.put("Message", "Order not found");
            return resp;
        }

        fields.remove("vnp_SecureHash");
        fields.remove("vnp_SecureHashType");

        boolean valid = VnPayUtil.verifySignature(fields, secureHash, hashSecret);
        if (!valid) {
            orderService.updatePayment(orderId, "FAILED", null);
            resp.put("RspCode", "97");
            resp.put("Message", "Invalid signature");
            return resp;
        }

        long expectedAmount = vnPayService.toVndAmount(order.getTotalPrice()) * 100;
        long paidAmount = 0;
        try {
            paidAmount = Long.parseLong(amountStr);
        } catch (Exception ignored) {
        }

        if (paidAmount != expectedAmount) {
            orderService.updatePayment(orderId, "FAILED", transactionNo);
            resp.put("RspCode", "04");
            resp.put("Message", "Amount mismatch");
            return resp;
        }

        if ("00".equals(responseCode)) {
            orderService.updatePayment(orderId, "PAID", transactionNo);
            resp.put("RspCode", "00");
            resp.put("Message", "Confirm Success");
        } else {
            orderService.updatePayment(orderId, "FAILED", transactionNo);
            resp.put("RspCode", "00");
            resp.put("Message", "Confirm Fail");
        }

        return resp;
    }

    private Map<String, String> extractParams(HttpServletRequest request) {
        Map<String, String> fields = new HashMap<>();
        Enumeration<String> params = request.getParameterNames();
        while (params.hasMoreElements()) {
            String key = params.nextElement();
            String value = request.getParameter(key);
            if (value != null && !value.isEmpty())
                fields.put(key, value);
        }
        return fields;
    }
}
