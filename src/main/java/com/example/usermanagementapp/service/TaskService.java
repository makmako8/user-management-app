package com.example.usermanagementapp.service;


import java.util.List;

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

    private final UserRepository userRepository;
    
    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    
    public void assignTaskToUser(Task task) {
        taskRepository.save(task);
    }



    public List<Task> getTasksAssignedTo(String username) {
        return taskRepository.findByAssignedToUsername(username);
    }
    public List<Task> getTasksForCurrentUser() {
        // ログイン中のユーザー名を取得
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // Repositoryを通じてユーザーIDに紐づくタスクを取得
        return taskRepository.findByAssignedToUsername(username);
    }
    public void saveTaskForUser(Task task, String username) {
        AppUser user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("ユーザーが見つかりません: " + username));

        task.setAssignedTo(user);
        taskRepository.save(task);
    }
}
