package com.example.usermanagementapp.config;

import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.example.usermanagementapp.security.JwtUtil;
@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {
	
    @Autowired
    private JwtUtil jwtUtil;
	
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        String username = authentication.getName();
        String token = jwtUtil.generateToken(username);

        // JWTをクッキーに保存（推奨） or レスポンスヘッダーで返す
        Cookie jwtCookie = new Cookie("JWT_TOKEN", token);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(60 * 60 * 24); // 1日
        response.addCookie(jwtCookie);

        // ログイン成功後のページへリダイレクト
        response.sendRedirect("/home");
    }
}
