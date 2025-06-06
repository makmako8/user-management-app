package com.example.usermanagementapp.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "TASK")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;

    private String status; // 未完了 / 完了

    private String description;
    private boolean completed;
    @ManyToOne
    @JoinColumn(name = "assigned_to") // ← DBの列名と一致
    private User assignedTo;
    
    public Task() {}

    public Task(String description) {
        this.description = description;
    }

    // getter, setter（後で追加してもOK）
}
