package com.example.usermanagementapp.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "progress_item") 
public class ProgressItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;       // 項目タイトル
    private String category;    // カテゴリ（例：1ヶ月目、2ヶ月目）
    private boolean completed;  // 完了フラグ
    
    // ★ ユーザーとの紐づけ
    private String username;
    
    public Long getId() {
    	return id;
    }
    public void setId(Long id) {
    	this.id =id;
    }
    public String getTitle() {
    	return title;
    }
    public void setTitle(String title) {
    	this.title =title;
    }
    public String getUsername() {
    	return username;
    }
    public void setUsername(String username) {
    	this.username =username;
    }
}

