package com.example.usermanagementapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.usermanagementapp.entity.User;
import com.example.usermanagementapp.repository.UserRepository;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/users")
    public String showUserList(Model model) {
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "admin/user-list"; // resources/templates/admin/user-list.html
    }
    @GetMapping("/admin/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminDashboard() {
        return "admin/dashboard"; // admin/dashboard.html に対応
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/users")
    public String showAllUsers(Model model) {
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "admin/user-list";
    }
}