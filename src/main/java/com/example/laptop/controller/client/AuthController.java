package com.example.laptop.controller.client;

import com.example.laptop.domain.dto.RegisterDTO;
import com.example.laptop.service.UserService;
import com.example.laptop.service.security.RegisterValidator;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final UserService userService;
    private final RegisterValidator registerValidator;

    public AuthController(UserService userService, RegisterValidator registerValidator) {
        this.userService = userService;
        this.registerValidator = registerValidator;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "client/auth/login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        if (!model.containsAttribute("registerDTO")) {
            model.addAttribute("registerDTO", new RegisterDTO());
        }
        return "client/auth/register";
    }

    @PostMapping("/register")
    public String handleRegister(
            @ModelAttribute("registerDTO") @Valid RegisterDTO registerDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        registerValidator.validate(registerDTO, bindingResult);

        if (bindingResult.hasErrors()) {
            return "client/auth/register";
        }

        // ✅ để service tự lo encode + set role USER
        userService.registerUser(registerDTO);

        redirectAttributes.addFlashAttribute("registerSuccess",
                "Registration successful! Please login.");
        return "redirect:/login";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "client/auth/deny";
    }

}
