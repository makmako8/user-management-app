package com.example.usermanagementapp.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class LoginController {

    @GetMapping("/home")
    public String home() {
        return "home"; // home.html を表示
    }
    @GetMapping("/login")
    public String showLoginForm() {
        return "login"; // resources/templates/login.html を表示
    }
    
}