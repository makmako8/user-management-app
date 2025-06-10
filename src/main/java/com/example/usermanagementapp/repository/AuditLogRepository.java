package com.example.usermanagementapp.repository;

import com.example.usermanagementapp.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
}
