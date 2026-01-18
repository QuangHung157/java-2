package com.example.laptop.controller.client;

import com.example.laptop.domain.Product;
import com.example.laptop.service.CartService;
import com.example.laptop.service.ProductService;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ItemController {

    private final ProductService productService;
    private final CartService cartService;

    public ItemController(ProductService productService, CartService cartService) {
        this.productService = productService;
        this.cartService = cartService;
    }

    private String currentEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    @GetMapping("/product/{id}")
    public String getProductPage(@PathVariable long id, Model model) {
        Product product = productService.getProductById(id);
        if (product == null)
            return "redirect:/";

        model.addAttribute("product", product);
        return "client/product/detail";
    }

    // This URL must be authenticated in SecurityConfiguration
    @PostMapping("/add-product-to-cart/{id}")
    public String addProductToCart(@PathVariable long id,
            @RequestParam(value = "quantity", defaultValue = "1") long quantity) {

        String email = currentEmail();
        cartService.addToCartByEmail(email, id, quantity);

        return "redirect:/cart";
    }
}
