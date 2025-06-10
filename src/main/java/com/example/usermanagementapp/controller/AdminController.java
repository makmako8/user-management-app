package com.example.usermanagementapp.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.usermanagementapp.entity.AppUser;
import com.example.usermanagementapp.entity.Task;
import com.example.usermanagementapp.repository.RoleRepository;
import com.example.usermanagementapp.repository.TaskRepository;
import com.example.usermanagementapp.repository.UserRepository;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
	
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final RoleRepository roleRepository;
    
    public AdminController(UserRepository userRepository, TaskRepository taskRepository, RoleRepository roleRepositor) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.roleRepository = roleRepositor;
    }

    // 管理者ダッシュボード
    @GetMapping("/dashboard")
    public String showAdminDashboard() {
        return "admin/dashboard"; // templates/admin-dashboard.html を返す
    }
    
    // ユーザー一覧表示
    @GetMapping("/users")
    public String showUserList(Model model) {
        List<AppUser> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "admin/user-list"; // resources/templates/admin/user-list.html
    }
    
    // タスク割り当てフォーム表示
    @GetMapping("/assign-task")
    public String showAssignTaskForm(Model model) {
        model.addAttribute("task", new Task());
        model.addAttribute("users", userRepository.findAll());
        return "admin/assign-task";
    }
    
    // タスク割り当て処理
    @PostMapping("/assign-task")
    public String assignTask(@ModelAttribute Task task, @RequestParam Long userId,
            RedirectAttributes redirectAttributes) {
        AppUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("ユーザーが見つかりません"));
        task.setAssignedTo(user);
        taskRepository.save(task);
        redirectAttributes.addFlashAttribute("successMessage", "課題を保存しました！");
        return "redirect:/admin/dashboard";
    }
    
    // ユーザー編集フォーム表示
    @GetMapping("/users/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        AppUser user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ユーザーが見つかりません: " + id));
        model.addAttribute("user", user);
        model.addAttribute("allRoles", roleRepository.findAll());
        return "admin/user-edit";
    }
    
    // ユーザー更新処理
    @PostMapping("/users/edit/{id}")
    public String updateUser(@PathVariable Long id, @ModelAttribute AppUser formUser) {
        AppUser existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ユーザーが見つかりません"));
        // ロール変更のログ出力
        System.out.println("✏️ 編集ユーザー: " + formUser.getUsername());
        System.out.println("🛡️ 編集前ロール:");
        existingUser.getRoles().forEach(role -> System.out.println(" - " + role.getRoleName()));
        
        System.out.println("🛡️ 編集後ロール:");
        formUser.getRoles().forEach(role -> System.out.println(" + " + role.getRoleName()));
        

        formUser.setId(id);
        userRepository.save(formUser);
        return "redirect:/admin/users";
    }

    // ユーザー削除処理
    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return "redirect:/admin/users";
    }

}