package com.example.usermanagementapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
@RequestMapping("/user")
public class UserHomeController {
    @GetMapping("/home")
    public String showHomePage() {
        return "home"; // templates/home.html を表示
    }

}
