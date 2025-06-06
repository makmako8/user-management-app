package com.example.usermanagementapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.usermanagementapp.entity.Task;
import com.example.usermanagementapp.entity.User;
import com.example.usermanagementapp.repository.TaskRepository;
import com.example.usermanagementapp.repository.UserRepository;
import com.example.usermanagementapp.service.TaskService;
@Controller
@RequestMapping("/user/tasks")
public class TaskController {


    @Autowired
    private TaskService taskService;
    
    @Autowired
    private UserRepository userRepository;
    
    
    @Autowired
    private TaskRepository taskRepository;
    @GetMapping
    public String listTasks(Authentication authentication, Model model) {
        String username = authentication.getName();
        List<Task> tasks = taskService.getTasksAssignedTo(username);
        model.addAttribute("tasks", tasks);
        return "user/task-list";
    }

    @GetMapping("/assign")
    public String showAssignForm(Model model) {
        model.addAttribute("task", new Task());
        model.addAttribute("users", userRepository.findAll());
        return "admin/assign-task"; // templates/admin/assign-task.html
    }
    @GetMapping("/user/tasks")
    public String showUserTasks(Authentication authentication, Model model) {
        String username = authentication.getName(); // ログイン中のユーザー名を取得
        List<Task> tasks = taskService.getTasksAssignedTo(username); // ユーザーに割り当てられた課題一覧
        model.addAttribute("tasks", tasks);
        return "user/task-list"; // テンプレートファイル（user/task-list.html）へ
    }
 // 新規作成フォーム表示
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("task", new Task());
        return "user/task-form";
    }
    @GetMapping("/tasks")
    public String showTasksForUser(Model model, Authentication authentication) {
        String username = authentication.getName();
        List<Task> tasks = taskService.getTasksAssignedTo(username);
        model.addAttribute("tasks", tasks);
        return "user/tasks";
    }
 // 新規タスク登録処理
    @PostMapping
    public String createTask(@ModelAttribute("task") Task task, Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("ユーザーが見つかりません: " + username));
        task.setAssignedTo(user);
        taskService.assignTaskToUser(task);
        return "redirect:/user/tasks";
    }

    @PostMapping("/assign")
    public String assignTask(@ModelAttribute Task task, @RequestParam("userId") Long userId) {
        User user = userRepository.findById(userId).orElse(null);
       if (user != null) {
            task.setAssignedTo(user);
          task.setCompleted(false);
            taskService.assignTaskToUser(task);
        }
       return "redirect:/admin/tasks/assign?success";
    }
   
}
