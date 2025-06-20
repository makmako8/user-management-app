package com.example.usermanagementapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class UserManagementAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserManagementAppApplication.class, args);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String rawPassword = "adminpass"; // ← ここを変更して好きなパスワードをハッシュ化
        String encoded = encoder.encode(rawPassword);

        System.out.println("元のパスワード: " + rawPassword);
        System.out.println("ハッシュ: " + encoded);
    }
	  
	

}
