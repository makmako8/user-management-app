package com.example.usermanagementapp.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.usermanagementapp.entity.Task;
import com.example.usermanagementapp.entity.User;


public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByAssignedTo(User user);
}
