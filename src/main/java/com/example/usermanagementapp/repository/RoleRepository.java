package com.example.usermanagementapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.usermanagementapp.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRoleName(String roleName);
}