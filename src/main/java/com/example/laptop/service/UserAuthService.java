package com.example.laptop.service;

import com.example.laptop.domain.User;
import com.example.laptop.repository.UserRepository;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserAuthService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserAuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        String normalizedEmail = (email == null) ? "" : email.trim().toLowerCase();

        User user = userRepository.findFirstByEmailOrderByIdAsc(normalizedEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Email not found: " + normalizedEmail));

        String roleName = (user.getRole() != null && user.getRole().getName() != null)
                ? user.getRole().getName().trim()
                : "USER";

        // chấp nhận cả "ADMIN" hoặc "ROLE_ADMIN"
        if (roleName.startsWith("ROLE_")) {
            roleName = roleName.substring("ROLE_".length());
        }

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .roles(roleName) // auto -> ROLE_roleName
                .build();
    }
}
