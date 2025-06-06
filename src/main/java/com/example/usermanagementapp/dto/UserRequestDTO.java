package com.example.usermanagementapp.dto;


public class UserRequestDTO {
    private String username;
    private String password;
    private String roleName;

    // getter/setter
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRoleName() { return roleName; }
    public void setRoleName(String roleName) { this.roleName = roleName; }
}
