package com.example.usermanagementapp.config;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {
	

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
    	System.out.println("✅ CustomLoginSuccessHandler が呼ばれました！");
    	System.out.println("🔐 ログインユーザー: " + authentication.getName());
    	System.out.println("🧾 ロール一覧:");
    	authentication.getAuthorities().forEach(auth -> System.out.println("→ " + auth.getAuthority()));

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        if (isAdmin) {
            response.sendRedirect("/admin/dashboard"); // ✅ ここに行かせたい！
            return;
        
        }
        boolean isUser = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER"));

        if (isUser) {
            response.sendRedirect("/user/home");
            return;
        }
        // ロールがなければデフォルト
        response.sendRedirect("/home");
    }
}
