package com.example.usermanagementapp.entity;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "APP_USER") 
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "USERNAME") 
    private String username;
    
    @Column(name = "PASSWORD") 
    private String password;
    private boolean enabled;
  
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "app_user_roles", // 中間テーブル名
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
        )
    // ゲッターとセッター
    // 必須：setter と getter

    private Set<Role> roles = new HashSet<>();
    
    public Set<Role> getRoles() {
        return roles;
    }
    public boolean isEnabled() {
        return enabled;
    }
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    
    public void setRoles(Set<Role> roles) {
    	this.roles = roles; 
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

 

}
