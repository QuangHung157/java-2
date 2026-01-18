package com.example.laptop.controller.admin;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.laptop.domain.User;
import com.example.laptop.service.UploadService;
import com.example.laptop.service.UserService;

@Controller
public class UserController {

    private final UserService userService;
    private final UploadService uploadService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService,
            UploadService uploadService,
            PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.uploadService = uploadService;
        this.passwordEncoder = passwordEncoder;
    }

    @RequestMapping("/admin/user")
    public String getUserPage(Model model) {
        List<User> users = this.userService.getAllUsers();
        model.addAttribute("users1", users);
        return "admin/user/show";
    }

    @RequestMapping("/admin/user/{id}")
    public String getUserDetailPage(Model model, @PathVariable long id) {
        User user = this.userService.getUserById(id);
        model.addAttribute("user", user);
        model.addAttribute("id", id);
        return "admin/user/detail";
    }

    @GetMapping("/admin/user/create")
    public String createUserPage(Model model) {
        model.addAttribute("newUser", new User());
        return "admin/user/create";
    }

    @PostMapping("/admin/user/create")
    public String postCreateUser(@ModelAttribute("newUser") User newUser,
            @RequestParam("File") MultipartFile file) {

        // ✅ 0) check email exists (không crash)
        if (newUser.getEmail() != null && userService.existsByEmail(newUser.getEmail())) {
            return "redirect:/admin/user/create?error=email-exists";
        }

        // ✅ 1) Upload avatar (optional)
        if (file != null && !file.isEmpty()) {
            String avatar = this.uploadService.handleSaveUploadFile(file, "avatar");
            newUser.setAvatar(avatar);
        }

        // ✅ 2) Hash password
        newUser.setPassword(this.passwordEncoder.encode(newUser.getPassword()));

        // ✅ 3) Set role theo name
        if (newUser.getRole() != null && newUser.getRole().getName() != null) {
            newUser.setRole(this.userService.getRoleByName(newUser.getRole().getName()));
        }

        // ✅ 4) Save
        this.userService.handleSaveUser(newUser);
        return "redirect:/admin/user";
    }

    @GetMapping("/admin/user/update/{id}")
    public String getUpdateUserPage(Model model, @PathVariable long id) {
        User currentUser = this.userService.getUserById(id);
        model.addAttribute("newUser", currentUser);
        return "admin/user/update";
    }

    @PostMapping("/admin/user/update")
    public String postUpdateUser(@ModelAttribute("newUser") User formUser) {

        User currentUser = this.userService.getUserById(formUser.getId());

        if (currentUser != null) {
            currentUser.setAddress(formUser.getAddress());
            currentUser.setFullName(formUser.getFullName());
            currentUser.setPhone(formUser.getPhone());

            this.userService.handleSaveUser(currentUser);
        }

        return "redirect:/admin/user";
    }

    @GetMapping("/admin/user/delete/{id}")
    public String getDeleteUserPage(Model model, @PathVariable long id) {
        model.addAttribute("id", id);
        model.addAttribute("newUser", new User());
        return "admin/user/delete";
    }

    @PostMapping("/admin/user/delete")
    public String postDeleteUser(@ModelAttribute("newUser") User formUser) {
        this.userService.deleteAUser(formUser.getId());
        return "redirect:/admin/user";
    }
}
