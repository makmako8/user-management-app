package com.example.usermanagementapp.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "quiz_result")
public class QuizResult {

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Long id;

 // 誰の結果か
 @ManyToOne
 @JoinColumn(name = "user_id")
 private AppUser user;

 // スコア
 private int score;

 // 出題数
 private int totalQuestions;

 // 正解数
 private int correctAnswers;

 // 解答日時
 private LocalDateTime answeredAt;

 // 備考（レベルなど）
 private String category;

 // --- getter / setter ---
 public Long getId() {
     return id;
 }

 public void setId(Long id) {
     this.id = id;
 }

 public AppUser getUser() {
     return user;
 }

 public void setUser(AppUser user) {
     this.user = user;
 }

 public int getScore() {
     return score;
 }

 public void setScore(int score) {
     this.score = score;
 }

 public int getTotalQuestions() {
     return totalQuestions;
 }

 public void setTotalQuestions(int totalQuestions) {
     this.totalQuestions = totalQuestions;
 }

 public int getCorrectAnswers() {
     return correctAnswers;
 }

 public void setCorrectAnswers(int correctAnswers) {
     this.correctAnswers = correctAnswers;
 }

 public LocalDateTime getAnsweredAt() {
     return answeredAt;
 }

 public void setAnsweredAt(LocalDateTime answeredAt) {
     this.answeredAt = answeredAt;
 }

 public String getCategory() {
     return category;
 }

 public void setCategory(String category) {
     this.category = category;
 }
}
