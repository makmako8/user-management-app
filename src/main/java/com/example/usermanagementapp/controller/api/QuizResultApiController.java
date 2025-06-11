package com.example.usermanagementapp.controller.api;


import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.usermanagementapp.dto.QuizResultRequestDTO;
import com.example.usermanagementapp.entity.AppUser;
import com.example.usermanagementapp.entity.QuizResult;
import com.example.usermanagementapp.repository.QuizResultRepository;
import com.example.usermanagementapp.repository.UserRepository;

@RestController
@RequestMapping("/api/quiz")
public class QuizResultApiController {

 @Autowired
 private UserRepository appUserRepository;

 @Autowired
 private QuizResultRepository quizResultRepository;

 @PostMapping("/answer")
 public String saveResult(@RequestBody QuizResultRequestDTO dto) {
     AppUser user = appUserRepository.findByUsername(dto.username)
         .orElseThrow(() -> new RuntimeException("ユーザーが見つかりません: " + dto.username));

     QuizResult result = new QuizResult();
     result.setUser(user);
     result.setScore(dto.score);
     result.setTotalQuestions(dto.totalQuestions);
     result.setCorrectAnswers(dto.correctAnswers);
     result.setCategory(dto.category);
     result.setAnsweredAt(LocalDateTime.now());

     quizResultRepository.save(result);
     return "✅ 採点結果を保存しました";
 }
}
