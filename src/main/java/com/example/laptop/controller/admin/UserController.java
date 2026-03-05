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
@RequestMapping("/admin/user")
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

    // =========================
    // LIST + SEARCH
    // =========================
    @GetMapping
    public String getUserPage(Model model,
            @RequestParam(value = "keyword", required = false) String keyword) {

        List<User> users = userService.searchUsersByName(keyword);

        model.addAttribute("users1", users);
        model.addAttribute("keyword", keyword == null ? "" : keyword);

        return "admin/user/show";
    }

    // =========================
    // DETAIL
    // =========================
    @GetMapping("/{id}")
    public String getUserDetailPage(Model model, @PathVariable long id) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        model.addAttribute("id", id);
        return "admin/user/detail";
    }

    // =========================
    // CREATE
    // =========================
    @GetMapping("/create")
    public String createUserPage(Model model) {
        model.addAttribute("newUser", new User());
        return "admin/user/create";
    }

    @PostMapping("/create")
    public String postCreateUser(@ModelAttribute("newUser") User newUser,
            @RequestParam("File") MultipartFile file) {

        if (newUser.getEmail() != null && userService.existsByEmail(newUser.getEmail())) {
            return "redirect:/admin/user/create?error=email-exists";
        }

        if (file != null && !file.isEmpty()) {
            String avatar = uploadService.handleSaveUploadFile(file, "avatar");
            newUser.setAvatar(avatar);
        }

        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));

        if (newUser.getRole() != null && newUser.getRole().getName() != null) {
            newUser.setRole(userService.getRoleByName(newUser.getRole().getName()));
        }

        userService.handleSaveUser(newUser);
        return "redirect:/admin/user";
    }

    // =========================
    // UPDATE
    // =========================
    @GetMapping("/update/{id}")
    public String getUpdateUserPage(Model model, @PathVariable long id) {
        User currentUser = userService.getUserById(id);
        model.addAttribute("newUser", currentUser);
        return "admin/user/update";
    }

    @PostMapping("/update")
    public String postUpdateUser(@ModelAttribute("newUser") User formUser) {

        User currentUser = userService.getUserById(formUser.getId());
        if (currentUser != null) {
            currentUser.setAddress(formUser.getAddress());
            currentUser.setFullName(formUser.getFullName());
            currentUser.setPhone(formUser.getPhone());
            userService.handleSaveUser(currentUser);
        }

        return "redirect:/admin/user";
    }

    // =========================
    // DELETE
    // =========================
    @GetMapping("/delete/{id}")
    public String getDeleteUserPage(Model model, @PathVariable long id) {
        model.addAttribute("id", id);
        model.addAttribute("newUser", new User());
        return "admin/user/delete";
    }

    @PostMapping("/delete")
    public String postDeleteUser(@ModelAttribute("newUser") User formUser) {
        userService.deleteAUser(formUser.getId());
        return "redirect:/admin/user";
    }
}
