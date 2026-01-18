package com.example.laptop.controller.client;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.laptop.domain.CartDetail;
import com.example.laptop.domain.Order;
import com.example.laptop.domain.OrderDetail;
import com.example.laptop.domain.User;
import com.example.laptop.service.CartService;
import com.example.laptop.service.OrderService;
import com.example.laptop.service.UserService;
import com.example.laptop.service.exception.OutOfStockException;

import jakarta.servlet.http.HttpSession;

@Controller
public class CartController {

    private final CartService cartService;
    private final OrderService orderService;
    private final UserService userService;

    public CartController(CartService cartService, OrderService orderService, UserService userService) {
        this.cartService = cartService;
        this.orderService = orderService;
        this.userService = userService;
    }

    private String currentEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    @GetMapping("/cart")
    public String viewCart(Model model, HttpSession session) {
        String email = currentEmail();

        List<CartDetail> cartDetails = cartService.getCartDetailsByEmail(email);
        BigDecimal totalPrice = cartService.calcTotalPrice(cartDetails); // ✅ BigDecimal

        model.addAttribute("cartDetails", cartDetails);
        model.addAttribute("totalPrice", totalPrice);

        session.setAttribute("sum", cartService.getSumByEmail(email));
        return "client/cart/cart";
    }

    @PostMapping("/cart/update")
    public String updateCart(@RequestParam("productId") long productId,
            @RequestParam("quantity") String quantityStr,
            HttpSession session) {

        String email = currentEmail();
        long quantity;

        try {
            quantity = Long.parseLong(quantityStr);
        } catch (NumberFormatException e) {
            quantity = 1;
        }

        if (quantity <= 0) {
            cartService.removeByProductByEmail(email, productId);
        } else {
            cartService.updateQuantityByEmail(email, productId, quantity);
        }

        session.setAttribute("sum", cartService.getSumByEmail(email));
        return "redirect:/cart";
    }

    @PostMapping("/cart/remove")
    public String removeFromCart(@RequestParam("productId") long productId, HttpSession session) {
        String email = currentEmail();

        cartService.removeByProductByEmail(email, productId);
        session.setAttribute("sum", cartService.getSumByEmail(email));

        return "redirect:/cart";
    }

    @GetMapping("/checkout")
    public String checkout(Model model, HttpSession session) {
        String email = currentEmail();

        List<CartDetail> cartDetails = cartService.getCartDetailsByEmail(email);
        if (cartDetails.isEmpty())
            return "redirect:/cart";

        BigDecimal totalPrice = cartService.calcTotalPrice(cartDetails); // ✅ BigDecimal

        model.addAttribute("cartDetails", cartDetails);
        model.addAttribute("totalPrice", totalPrice);

        session.setAttribute("sum", cartService.getSumByEmail(email));
        return "client/cart/checkout";
    }

    /**
     * ✅ Checkout submit:
     * - COD: create order then go to /thanks
     * - VNPAY: create order then redirect to /payment/vnpay/create?orderId=xxx
     */
    @PostMapping("/place-order")
    public String placeOrder(
            @RequestParam("receiverName") String receiverName,
            @RequestParam(value = "receiverAddress", required = false) String receiverAddress,
            @RequestParam("receiverPhone") String receiverPhone,
            @RequestParam("shippingMethod") String shippingMethod,
            @RequestParam("paymentMethod") String paymentMethod,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        String email = currentEmail();
        User user = userService.getUserByEmail(email);

        if (user == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Account not found.");
            return "redirect:/login";
        }

        try {
            Order order = orderService.placeUserOrder(
                    user,
                    receiverName,
                    receiverAddress,
                    receiverPhone,
                    shippingMethod,
                    paymentMethod);

            if (order == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "Cart is empty.");
                return "redirect:/cart";
            }

            session.setAttribute("sum", 0);

            if ("COD".equalsIgnoreCase(paymentMethod)) {
                return "redirect:/thanks?id=" + order.getId();
            }

            return "redirect:/payment/vnpay/create?orderId=" + order.getId();

        } catch (OutOfStockException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/checkout";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Something went wrong. Please try again.");
            return "redirect:/checkout";
        }
    }

    @GetMapping("/thanks")
    public String thanks(Model model, @RequestParam("id") long orderId, RedirectAttributes ra) {

        String email = currentEmail();

        Order order = orderService.getOrderById(orderId);
        if (order == null) {
            ra.addFlashAttribute("errorMessage", "Order not found.");
            return "redirect:/";
        }

        if (order.getUser() == null || !email.equalsIgnoreCase(order.getUser().getEmail())) {
            ra.addFlashAttribute("errorMessage", "You are not allowed to view this order.");
            return "redirect:/";
        }

        List<OrderDetail> orderDetails = orderService.getOrderDetails(orderId);

        model.addAttribute("order", order);
        model.addAttribute("orderDetails", orderDetails);

        return "client/cart/thanks";
    }
}
