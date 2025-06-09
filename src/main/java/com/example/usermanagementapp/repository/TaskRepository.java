package com.example.usermanagementapp.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.usermanagementapp.entity.Task;


public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query("SELECT t FROM Task t WHERE t.assignedTo.username = :username")
    List<Task> findByAssignedToUsername(@Param("username") String username);
}
