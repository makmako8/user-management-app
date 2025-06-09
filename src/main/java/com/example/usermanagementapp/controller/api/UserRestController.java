package com.example.usermanagementapp.controller.api;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.usermanagementapp.entity.AppUser;
import com.example.usermanagementapp.repository.UserRepository;

@RestController
@RequestMapping("/api/v2/users")
public class UserRestController {

    private final UserRepository userRepository;

    public UserRestController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // ユーザー一覧取得（GET /api/users）
    @GetMapping
    public List<AppUser> getAllUsers() {
        return userRepository.findAll();
    }
}