package com.example.usermanagementapp.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.usermanagementapp.entity.AuditLog;
import com.example.usermanagementapp.repository.AuditLogRepository;

@Controller
@PreAuthorize("hasRole('ADMIN')")
public class AuditLogController {

 private final AuditLogRepository auditLogRepository;

 public AuditLogController(AuditLogRepository auditLogRepository) {
     this.auditLogRepository = auditLogRepository;
 }

 @GetMapping("/admin/audit-log")
 public String showAuditLog(Model model) {
     List<AuditLog> logs = auditLogRepository.findAll(Sort.by(Sort.Direction.DESC, "timestamp"));
     model.addAttribute("auditLogs", logs);
     return "admin/audit-log";
 }

 @GetMapping("/admin/audit-log/export")
 public void exportAuditLogCsv(HttpServletResponse response) throws IOException {
     response.setContentType("text/csv");
     response.setHeader("Content-Disposition", "attachment; filename=audit-log.csv");

     List<AuditLog> logs = auditLogRepository.findAll(Sort.by(Sort.Direction.DESC, "timestamp"));
     PrintWriter writer = response.getWriter();
     writer.println("日時,ユーザー名,操作,ロール名");
     for (AuditLog log : logs) {
         writer.printf("%s,%s,%s,%s\n",
                 log.getTimestamp(), log.getUsername(), log.getAction(), log.getRoleName());
     }
     writer.flush();
 }

 @GetMapping("/admin/audit-log/filter")
 public String filterAuditLog(@RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
                              @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
                              Model model) {
     List<AuditLog> logs = auditLogRepository.findByTimestampBetween(from, to);
     model.addAttribute("auditLogs", logs);
     return "admin/audit-log";
 }
}
