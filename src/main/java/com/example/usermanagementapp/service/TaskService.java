package com.example.usermanagementapp.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.usermanagementapp.entity.Task;
import com.example.usermanagementapp.entity.User;
import com.example.usermanagementapp.repository.TaskRepository;
import com.example.usermanagementapp.repository.UserRepository;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;
    
    @Autowired
    private UserRepository userRepository; 

    public void assignTaskToUser(Task task) {
        taskRepository.save(task);
    }

    public List<Task> getTasksForUser(User user) {
        return taskRepository.findByAssignedTo(user);
    }

    public List<Task> getTasksAssignedTo(String username) {
    	  return userRepository.findByUsername(username)
    	            .map(taskRepository::findByAssignedTo)
    	            .orElseThrow(() -> new UsernameNotFoundException("ユーザーが見つかりません: " + username));
    }

}
