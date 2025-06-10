package com.example.usermanagementapp.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
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
import com.example.usermanagementapp.entity.AuditLog;
import com.example.usermanagementapp.entity.Task;
import com.example.usermanagementapp.repository.AuditLogRepository;
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
    private final AuditLogRepository auditLogRepository;
    
    public AdminController(UserRepository userRepository, TaskRepository taskRepository, RoleRepository roleRepositor, AuditLogRepository auditLogRepository) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.roleRepository = roleRepositor;
        this.auditLogRepository = auditLogRepository;
    }

    // 管理者ダッシュボード
    @GetMapping("/dashboard")
    public String showAdminDashboard() {
        return "admin/dashboard"; // templates/admin-dashboard.html を返す
    }
    @GetMapping("/audit-log")
    public String showAuditLog(Model model) {
        model.addAttribute("auditLogs", auditLogRepository.findAll(Sort.by(Sort.Direction.DESC, "timestamp")));
        return "admin/audit-log";
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
    
 // ✅ 差分ログ + 監査ログ保存付きユーザー編集処理
    @PostMapping("/users/edit/{id}")
    public String updateUser(@PathVariable Long id, @ModelAttribute AppUser formUser) {
        AppUser existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ユーザーが見つかりません"));
        	Set<String> beforeRoles = existingUser.getRoles().stream()
                .map(role -> role.getRoleName())
                .collect(Collectors.toSet());

            Set<String> afterRoles = formUser.getRoles().stream()
                .map(role -> role.getRoleName())
                .collect(Collectors.toSet());

            // 差分出力
            Set<String> addedRoles = new HashSet<>(afterRoles);
            addedRoles.removeAll(beforeRoles);
            Set<String> removedRoles = new HashSet<>(beforeRoles);
            removedRoles.removeAll(afterRoles);

            System.out.println("✏️ 編集ユーザー: " + formUser.getUsername());
            for (String added : addedRoles) {
                System.out.println("➕ 追加されたロール: " + added);
                auditLogRepository.save(new AuditLog("ADD_ROLE", formUser.getUsername(), added));
            }
            for (String removed : removedRoles) {
                System.out.println("➖ 削除されたロール: " + removed);
                auditLogRepository.save(new AuditLog("REMOVE_ROLE", formUser.getUsername(), removed));
            }
        

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