package com.example.usermanagementapp.controller;

import org.springframework.web.bind.annotation.GetMapping;

public class HomeController {
    @GetMapping("/home")
    public String showHomePage() {
        return "home"; // templates/home.html を表示
    }

}
