package com.example.usermanagementapp.entity;



import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    public Task() {}

    public Task(String description) {
        this.description = description;
    }

    // getter, setter（後で追加してもOK）
}
