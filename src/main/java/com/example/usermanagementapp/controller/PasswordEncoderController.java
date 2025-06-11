package com.example.usermanagementapp.controller;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
@Controller
public class PasswordEncoderController {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @GetMapping("/admin/encode-password")
    public String showEncodeForm() {
        return "admin/encode-password";
    }

    @PostMapping("/admin/encode-password")
    public String encodePassword(@RequestParam String rawPassword, Model model) {
        String encoded = passwordEncoder.encode(rawPassword);
        model.addAttribute("rawPassword", rawPassword);
        model.addAttribute("encodedPassword", encoded);
        return "admin/encode-password";
    }
}
