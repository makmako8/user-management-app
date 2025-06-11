package com.example.usermanagementapp.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
import com.example.usermanagementapp.service.TaskService;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
	@Autowired
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final RoleRepository roleRepository;
    private final AuditLogRepository auditLogRepository;
    @Autowired
    private TaskService taskService;
    
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
    @PostMapping("/admin/save-password")
    @PreAuthorize("hasRole('ADMIN')")
    public String saveEncodedPassword(@RequestParam String username,
                                      @RequestParam String hashedPassword,
                                      RedirectAttributes redirectAttributes) {
        AppUser user = new AppUser();
        user.setUsername(username);
        user.setPassword(hashedPassword);
        user.setEnabled(true);
        user.setRoles(Set.of(roleRepository.findByRoleName("ROLE_USER"))); // または ADMINも可
        userRepository.save(user);

        redirectAttributes.addFlashAttribute("successMessage", username + " を保存しました！");
        return "redirect:/admin/encode-password";
    }


    // ユーザー一覧表示
    @GetMapping("/users")
    public String showUserList(Model model) {
        List<AppUser> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "admin/user-list"; // resources/templates/admin/user-list.html
    }
    
    // GET: タスク割り当てフォーム表示（任意）
    @GetMapping("/assign")
    public String showAssignForm(Model model) {
        model.addAttribute("task", new Task());
        model.addAttribute("users", userRepository.findAll());
        return "admin/assign-task";
    }
    
    // POST: 他のユーザーにタスクを割り当て
    @PostMapping("/assign")
    public String assignTask(@ModelAttribute Task task, @RequestParam("userId") Long userId, Authentication authentication) {
        AppUser assignedUser = userRepository.findById(userId).orElse(null);
        if (assignedUser != null) {
            String creatorUsername = authentication.getName();
            AppUser creator = userRepository.findByUsername(creatorUsername)
                .orElseThrow(() -> new UsernameNotFoundException("作成者が見つかりません: " + creatorUsername));
            task.setAssignedTo(assignedUser);
            task.setCreatedBy(creator);  // 作成者を明示的に設定
            task.setCompleted(false);
            taskService.assignTaskToUser(task); 
        }
       return "redirect:/admin/tasks/assign?success";
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
        // パスワードが空の場合は、既存パスワードを維持
        if (formUser.getPassword() == null || formUser.getPassword().isBlank()) {
            formUser.setPassword(existingUser.getPassword());
        }
        // ロールの差分をチェック
        Set<String> beforeRoles = existingUser.getRoles() != null
            ? existingUser.getRoles().stream().map(r -> r.getRoleName()).collect(Collectors.toSet())
            : new HashSet<>();

        Set<String> afterRoles = formUser.getRoles() != null
            ? formUser.getRoles().stream().map(r -> r.getRoleName()).collect(Collectors.toSet())
            : new HashSet<>();
        
        	// 差分出力
	        Set<String> addedRoles = new HashSet<>(afterRoles);
	        addedRoles.removeAll(beforeRoles);
	        Set<String> removedRoles = new HashSet<>(beforeRoles);
	        removedRoles.removeAll(afterRoles);
	        // ログ出力＋監査ログ保存
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


    
    
}