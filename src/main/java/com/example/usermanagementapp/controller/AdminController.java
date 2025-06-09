package com.example.usermanagementapp.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.usermanagementapp.entity.AppUser;
import com.example.usermanagementapp.entity.Task;
import com.example.usermanagementapp.repository.TaskRepository;
import com.example.usermanagementapp.repository.UserRepository;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    
    public AdminController(UserRepository userRepository, TaskRepository taskRepository) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public String showUserList(Model model) {
        List<AppUser> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "admin/user-list"; // resources/templates/admin/user-list.html
    }
    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ADMIN')") // 管理者だけアクセス可能
    public String showAdminDashboard() {
        return "admin/dashboard"; // templates/admin-dashboard.html を返す
    }

    @GetMapping("/assign-task")
    @PreAuthorize("hasRole('ADMIN')")
    public String showAssignTaskForm(Model model) {
        model.addAttribute("task", new Task());
        model.addAttribute("users", userRepository.findAll());
        return "admin/assign-task";
    }
    @PostMapping("/assign-task")
    @PreAuthorize("hasRole('ADMIN')")
    public String assignTask(@ModelAttribute Task task, @RequestParam Long userId) {
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("ユーザーが見つかりません"));
        task.setAssignedTo(user);
        taskRepository.save(task);
        return "redirect:/admin/dashboard";
    }

}