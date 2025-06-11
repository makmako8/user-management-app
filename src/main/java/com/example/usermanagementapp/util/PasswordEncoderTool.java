package com.example.usermanagementapp.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderTool {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String rawPassword = "adminpass"; // ← ここを変更して好きなパスワードをハッシュ化
        String encoded = encoder.encode(rawPassword);

        System.out.println("元のパスワード: " + rawPassword);
        System.out.println("ハッシュ: " + encoded);
    }
}