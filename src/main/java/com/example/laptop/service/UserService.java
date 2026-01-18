package com.example.laptop.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.laptop.domain.Role;
import com.example.laptop.domain.User;
import com.example.laptop.domain.dto.RegisterDTO;
import com.example.laptop.repository.RoleRepository;
import com.example.laptop.repository.UserRepository;

@Service
public class UserService {

   private final UserRepository userRepository;
   private final RoleRepository roleRepository;
   private final PasswordEncoder passwordEncoder;

   public UserService(UserRepository userRepository,
         RoleRepository roleRepository,
         PasswordEncoder passwordEncoder) {
      this.userRepository = userRepository;
      this.roleRepository = roleRepository;
      this.passwordEncoder = passwordEncoder;
   }

   public List<User> getAllUsers() {
      return this.userRepository.findAll();
   }

   public User handleSaveUser(User user) {
      return this.userRepository.save(user);
   }

   public User getUserById(long id) {
      return this.userRepository.findById(id).orElse(null);
   }

   public void deleteAUser(long id) {
      this.userRepository.deleteById(id);
   }

   public Role getRoleByName(String name) {
      return this.roleRepository.findByName(name);
   }

   public boolean existsByEmail(String email) {
      String normalized = normalizeEmail(email);
      if (normalized == null)
         return false;
      return this.userRepository.existsByEmail(normalized);
   }

   public User getUserByEmail(String email) {
      String normalized = normalizeEmail(email);
      if (normalized == null)
         return null;
      return this.userRepository.findFirstByEmailOrderByIdAsc(normalized).orElse(null);
   }

   private String normalizeEmail(String email) {
      if (email == null)
         return null;
      String normalized = email.trim().toLowerCase();
      return normalized.isBlank() ? null : normalized;
   }

   /**
    * ✅ Production-ready register:
    * - normalize email
    * - check duplicate in service (double check)
    * - encode password
    * - set role USER (must exist)
    */
   @Transactional
   public User registerUser(RegisterDTO dto) {

      String email = normalizeEmail(dto.getEmail());
      if (email == null) {
         throw new IllegalArgumentException("Email is required");
      }

      // double-check duplicate (an toàn hơn cho production)
      if (userRepository.existsByEmail(email)) {
         throw new IllegalStateException("Email already exists");
      }

      Role userRole = roleRepository.findByName("USER");
      if (userRole == null) {
         throw new IllegalStateException("Role USER not found. Please seed roles first.");
      }

      User user = new User();
      user.setEmail(email);
      user.setFullName(dto.getFullName());
      user.setPhone(dto.getPhone());
      user.setAddress(dto.getAddress());
      user.setPassword(passwordEncoder.encode(dto.getPassword()));
      user.setRole(userRole);

      return userRepository.save(user);
   }
}
