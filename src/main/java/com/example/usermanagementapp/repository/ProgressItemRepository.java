package com.example.usermanagementapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.usermanagementapp.entity.ProgressItem;

public interface ProgressItemRepository extends JpaRepository<ProgressItem, Long> {
    List<ProgressItem> findByCategory(String category);
}
