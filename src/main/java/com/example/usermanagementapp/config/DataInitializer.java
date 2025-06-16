package com.example.usermanagementapp.config;


import java.util.HashSet;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.usermanagementapp.entity.AppUser;
import com.example.usermanagementapp.entity.Role;
import com.example.usermanagementapp.repository.RoleRepository;
import com.example.usermanagementapp.repository.UserRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // ロール登録
    	Role adminRole = roleRepository.findByRoleName("ROLE_ADMIN");
        if (!roleRepository.existsById(1L)) {
            roleRepository.save(new Role(1L, "ROLE_ADMIN"));
        }
        if (!roleRepository.existsById(2L)) {
            roleRepository.save(new Role(2L, "ROLE_USER"));
        }

        // 管理者ユーザー登録
        if (!userRepository.existsByUsername("admin")) {
            AppUser admin = new AppUser();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("adminpass"));
            Set<Role> roles = new HashSet<>();
            roles.add(adminRole);
            admin.setRoles(roles); 
            userRepository.save(admin);
        }
    }
}
