package com.example.laptop.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.laptop.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // check email exists
    boolean existsByEmail(String email);

    // get first user by email (avoid crash if duplicated data)
    Optional<User> findFirstByEmailOrderByIdAsc(String email);

    // find by email (returns null if not found)
    User findByEmail(String userEmail);

    // search by full name (ignore case)
    List<User> findByFullNameContainingIgnoreCase(String fullName);
}
