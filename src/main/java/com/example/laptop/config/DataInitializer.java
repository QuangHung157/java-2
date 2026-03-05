package com.example.laptop.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.laptop.domain.Role;
import com.example.laptop.domain.User;
import com.example.laptop.repository.RoleRepository;
import com.example.laptop.repository.UserRepository;

@Configuration
public class DataInitializer {

    @Value("${app.admin.email}")
    private String adminEmail;

    @Value("${app.admin.password}")
    private String adminPassword;

    @Value("${app.admin.full-name:Administrator}")
    private String adminFullName;

    @Bean
    CommandLineRunner initData(RoleRepository roleRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {

            Role adminRole = getOrCreateRole(roleRepository, "ADMIN", "System administrator");
            getOrCreateRole(roleRepository, "USER", "Normal user");

            String email = normalizeEmail(adminEmail);

            User admin = userRepository
                    .findFirstByEmailOrderByIdAsc(email)
                    .orElse(null);

            if (admin == null) {
                admin = new User();
                admin.setEmail(email);
                admin.setPassword(passwordEncoder.encode(adminPassword));
                admin.setFullName(adminFullName);
                admin.setRole(adminRole);

                userRepository.save(admin);
                System.out.println("✅ ADMIN created: " + email);
            } else if (!"ADMIN".equals(admin.getRole().getName())) {

                admin.setRole(adminRole);
                userRepository.save(admin);
                System.out.println("✅ Existing user upgraded to ADMIN: " + email);
            } else {
                System.out.println("ℹ️ ADMIN already exists: " + email);
            }
        };
    }

    private Role getOrCreateRole(RoleRepository repo, String name, String description) {
        Role role = repo.findByName(name);
        if (role == null) {
            role = new Role();
            role.setName(name);
            role.setDescription(description);
            repo.save(role);
            System.out.println("✅ Role created: " + name);
        }
        return role;
    }

    private String normalizeEmail(String email) {
        return email == null ? null : email.trim().toLowerCase();
    }
}
