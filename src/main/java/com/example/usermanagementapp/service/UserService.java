package com.example.usermanagementapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.usermanagementapp.entity.Role;
import com.example.usermanagementapp.entity.User;
import com.example.usermanagementapp.repository.RoleRepository;
import com.example.usermanagementapp.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleRepository roleRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public void registerUser(User user) {
        // パスワードのエンコード
    	   Role userRole = roleRepository.findByRoleName("ROLE_USER");
           if (userRole == null) {
               userRole = new Role();
               userRole.setRoleName("ROLE_USER");
               roleRepository.save(userRole);
           }
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new IllegalArgumentException("ユーザー名は既に存在します");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // ユーザーの役割をデフォルトで「USER」に設定
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            user.setRoles("USER");
        }
        // ユーザーを保存
        userRepository.save(user);
    }
}
