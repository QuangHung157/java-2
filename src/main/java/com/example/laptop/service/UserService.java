package com.example.laptop.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.laptop.domain.Cart;
import com.example.laptop.domain.Order;
import com.example.laptop.domain.Role;
import com.example.laptop.domain.User;
import com.example.laptop.domain.dto.RegisterDTO;
import com.example.laptop.repository.CartDetailRepository;
import com.example.laptop.repository.CartRepository;
import com.example.laptop.repository.OrderDetailRepository;
import com.example.laptop.repository.OrderRepository;
import com.example.laptop.repository.RoleRepository;
import com.example.laptop.repository.UserRepository;

@Service
public class UserService {

   private final UserRepository userRepository;
   private final RoleRepository roleRepository;
   private final PasswordEncoder passwordEncoder;

   // ✅ thêm để xóa sạch theo user
   private final CartRepository cartRepository;
   private final CartDetailRepository cartDetailRepository;
   private final OrderRepository orderRepository;
   private final OrderDetailRepository orderDetailRepository;

   public UserService(
         UserRepository userRepository,
         RoleRepository roleRepository,
         PasswordEncoder passwordEncoder,
         CartRepository cartRepository,
         CartDetailRepository cartDetailRepository,
         OrderRepository orderRepository,
         OrderDetailRepository orderDetailRepository) {

      this.userRepository = userRepository;
      this.roleRepository = roleRepository;
      this.passwordEncoder = passwordEncoder;

      this.cartRepository = cartRepository;
      this.cartDetailRepository = cartDetailRepository;
      this.orderRepository = orderRepository;
      this.orderDetailRepository = orderDetailRepository;
   }

   public List<User> getAllUsers() {
      return userRepository.findAll();
   }

   public User getUserById(long id) {
      return userRepository.findById(id).orElse(null);
   }

   public User handleSaveUser(User user) {
      return userRepository.save(user);
   }

   // =========================
   // SEARCH (admin)
   // =========================
   public List<User> searchUsersByName(String keyword) {
      if (keyword == null || keyword.trim().isBlank()) {
         return getAllUsers();
      }
      return userRepository.findByFullNameContainingIgnoreCase(keyword.trim());
   }

   // =========================
   // DELETE USER (demo: delete all data)
   // =========================
   @Transactional
   public void deleteAUser(long userId) {

      // 1) Xóa OrderDetail -> Order
      List<Order> orders = orderRepository.findByUserId(userId);
      if (orders != null && !orders.isEmpty()) {
         for (Order o : orders) {
            orderDetailRepository.deleteByOrderId(o.getId());
         }
         orderRepository.deleteAll(orders);
      }

      // 2) Xóa CartDetail -> Cart
      Cart cart = cartRepository.findByUserId(userId).orElse(null);
      if (cart != null) {
         cartDetailRepository.deleteByCartId(cart.getId());
         cartRepository.delete(cart);
      }

      // 3) Xóa User
      userRepository.deleteById(userId);
   }

   // =========================
   // ROLE / EMAIL HELPERS
   // =========================
   public Role getRoleByName(String name) {
      return roleRepository.findByName(name);
   }

   public boolean existsByEmail(String email) {
      String normalized = normalizeEmail(email);
      return normalized != null && userRepository.existsByEmail(normalized);
   }

   public User getUserByEmail(String email) {
      String normalized = normalizeEmail(email);
      return normalized == null ? null : userRepository.findFirstByEmailOrderByIdAsc(normalized).orElse(null);
   }

   private String normalizeEmail(String email) {
      if (email == null)
         return null;
      String normalized = email.trim().toLowerCase();
      return normalized.isBlank() ? null : normalized;
   }

   /**
    * register:
    * - normalize email
    * - check duplicate
    * - encode password
    * - set role USER
    */
   @Transactional
   public User registerUser(RegisterDTO dto) {

      String email = normalizeEmail(dto.getEmail());
      if (email == null)
         throw new IllegalArgumentException("Email is required");

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
