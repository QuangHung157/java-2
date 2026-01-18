package com.example.laptop.controller.admin;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.laptop.domain.Product;
import com.example.laptop.service.ProductService;
import com.example.laptop.service.UploadService;

@Controller
public class ProductController {

    private final ProductService productService;
    private final UploadService uploadService;

    public ProductController(ProductService productService, UploadService uploadService) {
        this.productService = productService;
        this.uploadService = uploadService;
    }

    // ===== LIST PRODUCT =====
    @GetMapping("/admin/product")
    public String getProductPage(Model model) {
        List<Product> products = this.productService.getAllProducts();
        model.addAttribute("products", products); // frontend đang dùng ${products}
        return "admin/product/show";
    }

    // ===== DETAIL =====
    @GetMapping("/admin/product/{id}")
    public String getProductDetailPage(Model model, @PathVariable long id) {
        Product product = this.productService.getProductById(id);
        model.addAttribute("product", product);
        return "admin/product/detail";
    }

    // ===== CREATE (GET) =====
    @GetMapping("/admin/product/create")
    public String createProductPage(Model model) {
        model.addAttribute("newProduct", new Product());
        return "admin/product/create";
    }

    // ===== CREATE (POST) =====
    @PostMapping("/admin/product/create")
    public String postCreateProduct(
            @ModelAttribute("newProduct") Product product,
            @RequestParam("File") MultipartFile file) {

        // frontend đã validate required => backend chỉ xử lý logic
        if (file != null && !file.isEmpty()) {
            String image = this.uploadService.handleSaveUploadFile(file, "product");
            product.setImage(image);
        }

        // sold mặc định
        product.setSold(0);

        this.productService.handleSaveProduct(product);
        return "redirect:/admin/product";
    }

    // ===== UPDATE (GET) =====
    @GetMapping("/admin/product/update/{id}")
    public String getUpdateProductPage(Model model, @PathVariable long id) {
        Product currentProduct = this.productService.getProductById(id);
        model.addAttribute("newProduct", currentProduct);
        return "admin/product/update";
    }

    // ===== UPDATE (POST) =====
    @PostMapping("/admin/product/update")
    public String postUpdateProduct(
            @ModelAttribute("newProduct") Product product,
            @RequestParam(value = "File", required = false) MultipartFile file) {

        Product currentProduct = this.productService.getProductById(product.getId());

        if (currentProduct != null) {
            currentProduct.setName(product.getName());
            currentProduct.setPrice(product.getPrice());
            currentProduct.setDetailDesc(product.getDetailDesc());
            currentProduct.setShortDesc(product.getShortDesc());
            currentProduct.setQuantity(product.getQuantity());
            currentProduct.setFactory(product.getFactory());
            currentProduct.setTarget(product.getTarget());

            // chỉ update ảnh nếu user chọn ảnh mới
            if (file != null && !file.isEmpty()) {
                String image = this.uploadService.handleSaveUploadFile(file, "product");
                currentProduct.setImage(image);
            }

            this.productService.handleSaveProduct(currentProduct);
        }

        return "redirect:/admin/product";
    }

    // ===== DELETE (GET) =====
    @GetMapping("/admin/product/delete/{id}")
    public String getDeleteProductPage(Model model, @PathVariable long id) {
        model.addAttribute("id", id);
        model.addAttribute("newProduct", new Product());
        return "admin/product/delete";
    }

    // ===== DELETE (POST) =====
    @PostMapping("/admin/product/delete")
    public String postDeleteProduct(@ModelAttribute("newProduct") Product product) {
        this.productService.deleteAProduct(product.getId());
        return "redirect:/admin/product";
    }
}
