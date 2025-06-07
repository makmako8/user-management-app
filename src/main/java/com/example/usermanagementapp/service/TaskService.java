package com.example.usermanagementapp.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.usermanagementapp.entity.Task;
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



    public List<Task> getTasksAssignedTo(String username) {
        return taskRepository.findByAssignedTo_Username(username);
    }

}
