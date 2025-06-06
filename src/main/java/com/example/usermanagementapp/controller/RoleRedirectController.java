package com.example.usermanagementapp.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RoleRedirectController {

    @GetMapping("/role-redirect")
    public String redirectBasedOnRole(Authentication authentication) {
        if (authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            return "redirect:/admin/users";
        } else {
            return "redirect:/user/home";
        }
    }
}