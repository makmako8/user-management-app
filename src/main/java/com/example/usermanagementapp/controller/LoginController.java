package com.example.usermanagementapp.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "login"; // login.html を表示
    }
    @GetMapping("/home")
    public String home() {
        return "home"; // home.html を表示
    }
}