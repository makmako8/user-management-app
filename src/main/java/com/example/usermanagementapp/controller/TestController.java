package com.example.usermanagementapp.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/user/test")
    @PreAuthorize("hasRole('USER')")
    public String userTest() {
        return "USER専用APIへのアクセスに成功しました！";
    }

    @GetMapping("/admin/test")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminTest() {
        return "ADMIN専用APIへのアクセスに成功しました！";
    }
}
