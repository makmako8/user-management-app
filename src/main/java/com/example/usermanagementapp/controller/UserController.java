package com.example.usermanagementapp.controller;






import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.usermanagementapp.entity.AppUser;
import com.example.usermanagementapp.repository.UserRepository;
import com.example.usermanagementapp.service.UserService;
@Controller
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;
    
    
    
    // ユーザー登録フォーム
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new AppUser());
        return "register";
    }
    @PostMapping("/register")
    public String registerUser(
        @ModelAttribute("user") AppUser user,
        @RequestParam("selectedRole") String selectedRole,
        Model model
    ) {
        try {
            userService.registerUserWithRole(user, selectedRole);
            
            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "登録に失敗しました: " + e.getMessage());
            return "register";
        }
    }


    @GetMapping("/admin/users-alt")
    @PreAuthorize("hasRole('ADMIN')")
    public String showAllUsers(Model model) {
        List<AppUser> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "user-list";
    }
	@PostMapping("/login")
	public String login(@RequestParam String username, @RequestParam String password, Model model) {
	    if ("admin".equals(username) && "pass".equals(password)) {
	        return "redirect:/home";
	    } else {
	        model.addAttribute("error", "ログイン失敗");
	        return "login";
	    }
	}


	




}
