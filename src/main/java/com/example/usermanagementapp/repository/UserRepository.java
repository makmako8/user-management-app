package com.example.usermanagementapp.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.usermanagementapp.entity.User;
public interface UserRepository extends JpaRepository<User, Long> {
//    Optional<User> findByUsername(String username);
	  User findByUsername(String username);
}