package com.example.laptop.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

public class RegisterDTO {

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@gmail.com$", message = "Chỉ chấp nhận email @gmail.com")
    private String email;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 8, message = "Mật khẩu phải tối thiểu 8 ký tự")
    private String password;

    @NotBlank(message = "Vui lòng nhập lại mật khẩu")
    private String confirmPassword;

    @NotBlank(message = "Họ tên không được để trống")
    private String fullName;

    private String phone;
    private String address;

    // ===== Custom validate =====
    public boolean isPasswordMatched() {
        return password != null && password.equals(confirmPassword);
    }

    // ===== Getter / Setter =====
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
