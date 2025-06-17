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
        // ロールが存在しなければ登録（ここでは ID は自動生成でもOK）
        if (!roleRepository.existsByRoleName("ROLE_ADMIN")) {
            roleRepository.save(new Role(null, "ROLE_ADMIN"));
        }
        if (!roleRepository.existsByRoleName("ROLE_USER")) {
            roleRepository.save(new Role(null, "ROLE_USER"));
        }
        // ロールを取得（登録後に確実に取得！）
        // 登録後、もう一度取得（Optionalに対応）
        Role adminRole = roleRepository.findByRoleName("ROLE_ADMIN")
        	    .orElseThrow(() -> new RuntimeException("ROLE_ADMIN が見つかりません"));

        	System.out.println("adminRole: " + adminRole.getRoleName());
       
        // 管理者ユーザー登録
        if (!userRepository.existsByUsername("admin")) {
            AppUser admin = new AppUser();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("adminpass"));
            admin.setEnabled(true);
            
            Set<Role> roles = new HashSet<>();
            roles.add(adminRole);  
            admin.setRoles(roles);
            userRepository.save(admin);
                }
     // ROLE_USERを取得
        Role userRole = roleRepository.findByRoleName("ROLE_USER")
            .orElseThrow(() -> new RuntimeException("ROLE_USER が見つかりません"));
        if (!userRepository.existsByUsername("testuser7")) {
            AppUser user = new AppUser();
            user.setUsername("testuser7");
            user.setPassword(passwordEncoder.encode("testpass7"));
            user.setEnabled(true);
            user.setRoles(Set.of(userRole)); // ロールを割り当てる
            userRepository.save(user);
        }
    }
}
