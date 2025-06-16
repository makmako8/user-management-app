package com.example.usermanagementapp.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.usermanagementapp.entity.AppUser;


public interface UserRepository extends JpaRepository<AppUser, Long> {
   Optional<AppUser> findByUsername(String username);
   boolean existsByUsername(String username);
}