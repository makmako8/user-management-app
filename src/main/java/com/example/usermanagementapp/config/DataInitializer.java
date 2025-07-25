package com.example.usermanagementapp.config;


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
        initializeRoles();
        initializeUsers();
    }

    private void initializeRoles() {
        createRoleIfNotExists("ROLE_ADMIN");
        createRoleIfNotExists("ROLE_USER");
    }
    
    private void initializeUsers() {
        Role adminRole = roleRepository.findByRoleName("ROLE_ADMIN")
                .orElseThrow(() -> new RuntimeException("ROLE_ADMINが見つかりません"));
        createUserIfNotExists("admin", "adminpass", adminRole);

        Role userRole = roleRepository.findByRoleName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("ROLE_USERが見つかりません"));
        createUserIfNotExists("testuser7", "testpass7", userRole);
        createUserIfNotExists("testuser8", "testpass8", userRole);
        createUserIfNotExists("testuser9", "testpass9", userRole);
    }

    private void createRoleIfNotExists(String roleName) {
        if (!roleRepository.existsByRoleName(roleName)) {
            roleRepository.save(new Role(null, roleName));
        }
    }

    private void createUserIfNotExists(String username, String password, Role role) {
        if (!userRepository.existsByUsername(username)) {
            AppUser user = new AppUser();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(password));
            user.setEnabled(true);
            user.setRoles(Set.of(role));
            userRepository.save(user);
        }
    }
}
