package com.example.laptop.controller.client;

import com.example.laptop.domain.Product;
import com.example.laptop.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ShopController {

    private final ProductService productService;

    public ShopController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public String getProductPage(Model model,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "factory", required = false) List<String> factory,
            @RequestParam(name = "target", required = false) List<String> target,
            @RequestParam(name = "price", required = false) List<String> price,
            @RequestParam(name = "sort", required = false) String sort) {

        int pageNo = page - 1;
        int pageSize = 6; // Hiển thị 6 sản phẩm mỗi trang

        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("id").descending()); // Mặc định: Mới nhất lên đầu

        if (sort != null) {
            if (sort.equals("price-asc")) {
                pageable = PageRequest.of(pageNo, pageSize, Sort.by("price").ascending());
            } else if (sort.equals("price-desc")) {
                pageable = PageRequest.of(pageNo, pageSize, Sort.by("price").descending());
            }
        }

        Page<Product> productPage = productService.filterProducts(factory, target, price, pageable);

        List<Product> products = productPage.getContent();

        model.addAttribute("products", products);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());

        model.addAttribute("selectedFactories", factory != null ? factory : new ArrayList<>());
        model.addAttribute("selectedTargets", target != null ? target : new ArrayList<>());
        model.addAttribute("selectedPrices", price != null ? price : new ArrayList<>());
        model.addAttribute("selectedSort", sort);

        return "client/product/show";
    }
}