package com.example.usermanagementapp.controller;

import java.util.List;

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

import com.example.usermanagementapp.entity.AppUser;
import com.example.usermanagementapp.entity.Task;
import com.example.usermanagementapp.repository.TaskRepository;
import com.example.usermanagementapp.repository.UserRepository;
import com.example.usermanagementapp.service.TaskService;
@Controller
public class TaskController {


    @Autowired
    private TaskService taskService;
    
    @Autowired
    private UserRepository userRepository;
    
    
    @Autowired
    private TaskRepository taskRepository;
    
    @GetMapping("/assign")
    public String showAssignForm(Model model) {
        model.addAttribute("task", new Task());
        model.addAttribute("users", userRepository.findAll());
        return "admin/assign-task"; // templates/admin/assign-task.html
    }
    @GetMapping("/user/tasks")
        public String showTasksForCurrentUser(Authentication authentication, Model model) {
        String username = authentication.getName();
        List<Task> tasks = taskService.getTasksAssignedTo(username);
        model.addAttribute("tasks", tasks);
        return "user/task-list";
    }


 // 新規作成フォーム表示
    @GetMapping("/user/tasks/new")
    @PreAuthorize("hasRole('USER')")
    public String showCreateForm(Model model) {
        model.addAttribute("task", new Task());
        return "user/task-form";
    }  

    @PostMapping("/user/tasks/new")
    @PreAuthorize("hasRole('USER')")
    public String createOwnTask(@ModelAttribute Task task, Authentication authentication) {
        String username = authentication.getName();
        AppUser user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("ユーザーが見つかりません: " + username));

        task.setAssignedTo(user); // 自分に割り当て
        task.setCreatedBy(user);  // 自分が作成者
        task.setCompleted(false);

        taskService.assignTaskToUser(task);

        return "redirect:/user/tasks";
    }


    @GetMapping("/tasks")
    public String showTasksForUser(Authentication authentication, Model model) {
        String username = authentication.getName();
        List<Task> tasks = taskService.getTasksAssignedTo(username);
        model.addAttribute("tasks", tasks);
        return "user/tasks";
    }


    @GetMapping("/user/tasks/edit/{id}")
    @PreAuthorize("hasRole('USER')")
    public String showEditForm(@PathVariable Long id, Model model, Authentication authentication) {
        Task task = taskService.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("タスクが見つかりません: ID = " + id));
        String currentUsername = authentication.getName();
        if (!task.getAssignedTo().getUsername().equals(currentUsername)) {
            return "redirect:/user/tasks?error=not_authorized";
        }
        model.addAttribute("task", task);
        return "user/task-form"; // フォームを使いまわし
    }
    
    @PostMapping("/user/tasks/edit/{id}")
    @PreAuthorize("hasRole('USER')")
    public String updateTask(@PathVariable Long id,
                             @ModelAttribute("task") Task updatedTask,
                             Authentication authentication) {

        Task existingTask = taskService.findById(id)
            .orElseThrow(() -> new RuntimeException("タスクが見つかりません"));

        String currentUsername = authentication.getName();
        if (!existingTask.getAssignedTo().getUsername().equals(currentUsername)) {
            return "redirect:/user/tasks?error=not_authorized";
        }

        // 必要なフィールドを更新（IDや作成者はそのまま）
        existingTask.setTitle(updatedTask.getTitle());
        existingTask.setDescription(updatedTask.getDescription());
        existingTask.setCompleted(updatedTask.isCompleted());

        taskService.assignTaskToUser(existingTask);
        return "redirect:/user/tasks";
    }
    @GetMapping("/user/tasks/delete/{id}")
    @PreAuthorize("hasRole('USER')")
    public String deleteTask(@PathVariable Long id, Authentication authentication) {
        Task task = taskService.findById(id)
            .orElseThrow(() -> new RuntimeException("タスクが見つかりません"));

        String currentUsername = authentication.getName();
        if (!task.getAssignedTo().getUsername().equals(currentUsername)) {
            return "redirect:/user/tasks?error=not_authorized";
        }

        taskService.deleteTask(id);
        return "redirect:/user/tasks";
    }

}
