package com.example.usermanagementapp.dto;


public class ReviewRequestDTO {
    private Long questionId;
    private String reviewType; // "same", "rephrased", "applied"

    // getter/setter
    public Long getQuestionId() { return questionId; }
    public void setQuestionId(Long questionId) { this.questionId = questionId; }

    public String getReviewType() { return reviewType; }
    public void setReviewType(String reviewType) { this.reviewType = reviewType; }
}
