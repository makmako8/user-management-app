package com.example.usermanagementapp.controller;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.usermanagementapp.entity.AppUser;
import com.example.usermanagementapp.entity.Role;
import com.example.usermanagementapp.repository.RoleRepository;
import com.example.usermanagementapp.repository.UserRepository;

@Controller
public class PasswordSaveController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public PasswordSaveController(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @PostMapping("/admin/save-password")
    @PreAuthorize("hasRole('ADMIN')")
    public String saveEncodedPassword(@RequestParam String username,
                                      @RequestParam String hashedPassword,
                                      RedirectAttributes redirectAttributes) {
        Optional<AppUser> existing = userRepository.findByUsername(username);
        if (existing.isPresent()) {
            redirectAttributes.addFlashAttribute("errorMessage", "既に存在するユーザー名です。");
            return "redirect:/admin/encode-password";
        }

        AppUser user = new AppUser();
        user.setUsername(username);
        user.setPassword(hashedPassword);
        user.setEnabled(true);

        Role defaultRole = roleRepository.findByRoleName("ROLE_USER");
        Set<Role> roles = new HashSet<>();
        roles.add(defaultRole);
        user.setRoles(roles);

        userRepository.save(user);
        redirectAttributes.addFlashAttribute("successMessage", username + " を保存しました！");
        return "redirect:/admin/encode-password";
    }
}



