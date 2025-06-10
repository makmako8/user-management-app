package com.example.usermanagementapp.controller.api;

import java.security.Principal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.usermanagementapp.entity.AppUser;
import com.example.usermanagementapp.entity.Task;
import com.example.usermanagementapp.repository.TaskRepository;
import com.example.usermanagementapp.service.UserService;

@RestController
@RequestMapping("/api")
public class ApiUserTaskController {

	 private final UserService userService;
	 private final TaskRepository taskRepository;

	 public ApiUserTaskController(UserService userService, TaskRepository taskRepository) {
	        this.userService = userService;
	        this.taskRepository = taskRepository;
	    }

    // 🔹 ユーザー一覧取得（管理者のみ）
    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public List<AppUser> getAllUsers() {
        return userService.findAllUsers();
    }

    // 🔹 ログインユーザーのタスク一覧取得
    @GetMapping("/tasks")
    @PreAuthorize("hasRole('USER')")
    public List<Task> getTasksForUser(Principal principal) {
        AppUser user = userService.getUserByUsername(principal.getName());
        return taskRepository.findByAssignedTo(user);
    }

    // 🔹 タスク登録（自分に紐づけ）
    @PostMapping("/tasks")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Task> createTask(@RequestBody Task task, Principal principal) {
        AppUser user = userService.getUserByUsername(principal.getName());
        task.setAssignedTo(user);
        Task saved = taskRepository.save(task);
        return ResponseEntity.ok(saved);
    }

    // 🔹 タスク削除（自分のものだけ）
    @DeleteMapping("/tasks/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id, Principal principal) {
        AppUser user = userService.getUserByUsername(principal.getName());
        Task task = taskRepository.findById(id).orElseThrow();
        if (!task.getAssignedTo().getId().equals(user.getId())) {
            return ResponseEntity.status(403).build();
        }
        taskRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
