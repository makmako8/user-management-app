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
import org.springframework.web.bind.annotation.RequestParam;

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
    	   System.out.println("📝 編集内容: " + updatedTask); // ← これで値確認
    	  
    	   Task task = taskService.findById(id)
    		        .orElseThrow(() -> new RuntimeException("タスクが見つかりません"));

    		    String username = authentication.getName();
    		    if (!task.getAssignedTo().getUsername().equals(username)) {
    		        return "redirect:/user/tasks?error=unauthorized";
    		    }


    		    // 更新する内容のみセット
    		    task.setTitle(updatedTask.getTitle());
    		    task.setDescription(updatedTask.getDescription());
    		    task.setCompleted(updatedTask.isCompleted());

        taskService.assignTaskToUser(task);
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
    @PostMapping("/user/tasks/toggle/{id}")
    @PreAuthorize("hasRole('USER')")
    public String toggleTaskCompletion(@PathVariable Long id, Authentication authentication) {
        Task task = taskService.findById(id)
            .orElseThrow(() -> new RuntimeException("タスクが見つかりません"));

        if (!task.getAssignedTo().getUsername().equals(authentication.getName())) {
            return "redirect:/user/tasks?error=not_authorized";
        }

        task.setCompleted(!task.isCompleted());
        taskService.assignTaskToUser(task);
        return "redirect:/user/tasks";
    }
    @GetMapping("/user/tasks")
    @PreAuthorize("hasRole('USER')")
    public String showFilteredTasks(@RequestParam(value = "keyword", required = false) String keyword,
                                    @RequestParam(value = "status", required = false) String status,
                                    Authentication authentication,
                                    Model model) {

        String username = authentication.getName();
        AppUser user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("ユーザーが見つかりません"));

        List<Task> tasks;

        // 状態とキーワードに応じて検索
        if ("completed".equals(status)) {
            tasks = taskRepository.findByAssignedToAndCompletedTrueAndTitleContaining(user, keyword != null ? keyword : "");
        } else if ("incomplete".equals(status)) {
            tasks = taskRepository.findByAssignedToAndCompletedFalseAndTitleContaining(user, keyword != null ? keyword : "");
        } else {
            tasks = taskRepository.findByAssignedToAndTitleContaining(user, keyword != null ? keyword : "");
        }

        model.addAttribute("tasks", tasks);
        model.addAttribute("keyword", keyword);
        model.addAttribute("status", status);

        return "user/task-list";
    }

}
