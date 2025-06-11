package com.example.usermanagementapp.dto;


public class QuizResultRequestDTO {
    public String username;
    public int score;
    public int totalQuestions;
    public int correctAnswers;
    public String category; // 例：Bronze, Silver, Java基礎など
}