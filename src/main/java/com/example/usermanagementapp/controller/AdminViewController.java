package com.example.usermanagementapp.controller;

import java.util.List;

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
import com.example.usermanagementapp.entity.Role;
import com.example.usermanagementapp.entity.Task;
import com.example.usermanagementapp.repository.RoleRepository;
import com.example.usermanagementapp.repository.TaskRepository;
import com.example.usermanagementapp.repository.UserRepository;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminViewController {

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final RoleRepository roleRepository;

    public AdminViewController(UserRepository userRepository, TaskRepository taskRepository, RoleRepository roleRepository
    		) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.roleRepository = roleRepository;
    }

    @GetMapping("/assign-task")
    public String showAssignTaskForm(Model model) {
        model.addAttribute("task", new Task());
        model.addAttribute("users", userRepository.findAll());
        return "admin/assign-task-form"; // templates/admin/assign-task-form.html
    }

    
    @PostMapping("/assign-task")
    public String assignTask(@ModelAttribute Task task, 
    						 @RequestParam("assignedTo") Long userId,
    						 Authentication authentication) {
        AppUser admin = userRepository.findByUsername(authentication.getName())
            .orElseThrow(() -> new UsernameNotFoundException("Admin user not found"));

        AppUser assignedUser = userRepository.findById(userId)
            .orElseThrow(() -> new UsernameNotFoundException("Assigned user not found"));

        task.setCreatedBy(admin);
        task.setAssignedTo(assignedUser);

        taskRepository.save(task);

        return "redirect:/admin/users";
    }
    
    @GetMapping("/users")
    public String showUserList(Model model) {
        List<AppUser> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "admin/user-list";
    }
    
 // ユーザー編集フォーム表示
    @GetMapping("/users/edit/{id}")
    public String editUserForm(@PathVariable Long id, Model model) {
        AppUser user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("ユーザーが見つかりません (ID: " + id + ")"));
        List<Role> allRoles = roleRepository.findAll();
        model.addAttribute("user", user);
        model.addAttribute("allRoles", allRoles);
        return "admin/user-edit"; // templates/admin/user-edit.html
    }

    // ユーザー更新処理
    @PostMapping("/users/edit/{id}")
    public String updateUser(@PathVariable Long id, @ModelAttribute AppUser updatedUser, RedirectAttributes redirectAttributes) {
        AppUser user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("ユーザーが見つかりません (ID: " + id + ")"));

        user.setUsername(updatedUser.getUsername());
        user.setEnabled(updatedUser.isEnabled());
        user.setRoles(updatedUser.getRoles());

        userRepository.save(user);

        redirectAttributes.addFlashAttribute("successMessage", "ユーザーを更新しました。");
        return "redirect:/admin/users";
    }

    // ユーザー削除処理
    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        AppUser user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("ユーザーが見つかりません (ID: " + id + ")"));

        userRepository.delete(user);

        redirectAttributes.addFlashAttribute("successMessage", "ユーザーを削除しました。");
        return "redirect:/admin/users";
    }
    @GetMapping("/users/{id}/tasks")
    public String showUserTasks(@PathVariable Long id, Model model) {
        AppUser user = userRepository.findById(id)
            .orElseThrow(() -> new UsernameNotFoundException("ユーザーが見つかりません"));
        List<Task> tasks = taskRepository.findByAssignedTo(user);
        model.addAttribute("tasks", tasks);
        model.addAttribute("user", user);
        return "admin/user-task-list";
    }
    // ユーザー別タスク一覧表示
    @GetMapping("/tasks/edit/{id}")
    public String showEditTaskForm(@PathVariable Long id, Model model) {
        Task task = taskRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("タスクが見つかりません"));
        model.addAttribute("task", task);
        return "admin/task-edit";
    }
    
    // タスク更新処理
    @PostMapping("/tasks/edit/{id}")
    public String updateTask(@PathVariable Long id, @ModelAttribute Task updatedTask) {
        Task task = taskRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("タスクが見つかりません"));
        task.setTitle(updatedTask.getTitle());
        task.setDescription(updatedTask.getDescription());
        task.setCompleted(updatedTask.isCompleted());
        taskRepository.save(task);
        return "redirect:/admin/users/" + task.getAssignedTo().getId() + "/tasks";
    }

    // タスク削除処理
    @GetMapping("/tasks/delete/{id}")
    public String deleteTask(@PathVariable Long id) {
        Task task = taskRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("タスクが見つかりません"));
        Long userId = task.getAssignedTo().getId();
        taskRepository.delete(task);
        return "redirect:/admin/users/" + userId + "/tasks";
    }

}
