package com.example.usermanagementapp.service;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.usermanagementapp.entity.AppUser;
import com.example.usermanagementapp.entity.Task;
import com.example.usermanagementapp.repository.TaskRepository;
import com.example.usermanagementapp.repository.UserRepository;
@Service
public class TaskService {
 
    private final TaskRepository taskRepository;
    @Autowired
    private final UserRepository userRepository;
    
    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public Task createTask(Task task) {
        // 認証ユーザー名（作成者）を取得してセット
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        AppUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("ユーザーが見つかりません: " + username));
        task.setCreatedBy(user);
        return taskRepository.save(task);
    }
    public void assignTaskToUser(Task task) {
    	   taskRepository.save(task);
           }
    public Task assignTaskToUser(Task task, AppUser user) {
        task.setCreatedBy(user);
        task.setAssignedTo(user);
        return taskRepository.save(task);
    }
    public Optional<Task> findById(Long id) {
        return taskRepository.findById(id);
    }


    public List<Task> getTasksAssignedTo(String username) {
        AppUser user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("ユーザーが見つかりません"));
        return taskRepository.findByAssignedTo(user);
    }

    public List<Task> getTasksForCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        AppUser user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("ユーザーが見つかりません"));
        return taskRepository.findByAssignedTo(user);
    }
    public void saveTaskForUser(Task task, String username) {
        AppUser user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("ユーザーが見つかりません: " + username));

        task.setAssignedTo(user);
        taskRepository.save(task);
    }
    public void updateTask(Long id, Task updatedTask) {
        Task existing = taskRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("タスクが存在しません"));
        
        existing.setTitle(updatedTask.getTitle());
        existing.setDescription(updatedTask.getDescription());
        existing.setCompleted(updatedTask.isCompleted());

        taskRepository.save(existing);
    }

public void deleteTask(Long id) {
    taskRepository.deleteById(id);
}
}
