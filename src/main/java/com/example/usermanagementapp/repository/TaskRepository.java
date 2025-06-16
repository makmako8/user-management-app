package com.example.usermanagementapp.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.usermanagementapp.entity.AppUser;
import com.example.usermanagementapp.entity.Task;
public interface TaskRepository extends JpaRepository<Task, Long> {
	List<Task> findByCreatedBy(AppUser createdBy);
	List<Task> findByAssignedTo(AppUser assignedTo);
    List<Task> findByAssignedToAndCompletedTrue(AppUser user);
    List<Task> findByAssignedToAndCompletedFalse(AppUser user);
    List<Task> findByAssignedToAndTitleContaining(AppUser user, String keyword);
    List<Task> findByAssignedToAndCompletedTrueAndTitleContaining(AppUser user, String keyword);
    List<Task> findByAssignedToAndCompletedFalseAndTitleContaining(AppUser user, String keyword);
    List<Task> findByAssignedToOrderByCreatedAtAsc(AppUser user);
    long countByAssignedToAndCompletedFalse(AppUser assignedTo);
}