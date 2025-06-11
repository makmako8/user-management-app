package com.example.usermanagementapp.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.usermanagementapp.entity.AuditLog;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
	List<AuditLog> findByTimestampBetween(LocalDateTime from, LocalDateTime to);

}
