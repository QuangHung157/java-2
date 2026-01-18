package com.example.laptop.service.security;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.example.laptop.domain.dto.RegisterDTO;
import com.example.laptop.service.UserService;

@Component
public class RegisterValidator implements Validator {

    private final UserService userService;

    public RegisterValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return RegisterDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        RegisterDTO dto = (RegisterDTO) target;

        // 1) confirm password
        if (dto.getPassword() != null && dto.getConfirmPassword() != null
                && !dto.getPassword().equals(dto.getConfirmPassword())) {
            errors.rejectValue("confirmPassword", "password.mismatch", "Xác nhận mật khẩu không khớp");
        }

        // 2) email exists
        String email = dto.getEmail();
        if (email != null) {
            email = email.trim().toLowerCase();
            if (userService.existsByEmail(email)) {
                errors.rejectValue("email", "email.exists", "Email đã được đăng ký");
            }
        }
    }
}
