package com.example.usermanagementapp.controller.api;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.usermanagementapp.entity.AppUser;
import com.example.usermanagementapp.repository.UserRepository;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminApiController {
	
    private final UserRepository userRepository;


    public AdminApiController(UserRepository userRepository) {
        this.userRepository = userRepository;
   
    }

    @GetMapping("/users")
    public ResponseEntity<List<AppUser>> getAllUsers() {
        List<AppUser> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    // 他のAPI用メソッドをここに追加可能です。
}
