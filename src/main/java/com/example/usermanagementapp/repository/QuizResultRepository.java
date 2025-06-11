package com.example.usermanagementapp.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.usermanagementapp.entity.AppUser;
import com.example.usermanagementapp.entity.QuizResult;

public interface QuizResultRepository extends JpaRepository<QuizResult, Long> {
 List<QuizResult> findByUser(AppUser user);
}
