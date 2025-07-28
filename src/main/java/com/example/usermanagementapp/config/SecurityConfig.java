package com.example.usermanagementapp.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.usermanagementapp.repository.UserRepository;
import com.example.usermanagementapp.security.CustomAuthenticationProvider;
import com.example.usermanagementapp.security.JwtRequestFilter;
import com.example.usermanagementapp.security.JwtUtil;
@Configuration
@EnableWebSecurity
public class SecurityConfig{
	
    private final CustomAuthenticationProvider customAuthenticationProvider;
    private final CustomLoginSuccessHandler customLoginSuccessHandler;
    private final JwtRequestFilter jwtRequestFilter;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    
    @Autowired
    public SecurityConfig(
            CustomAuthenticationProvider customAuthenticationProvider,
            CustomLoginSuccessHandler customLoginSuccessHandler,
            JwtRequestFilter jwtRequestFilter,
            JwtUtil jwtUtil,
            UserRepository userRepository) {
        this.customAuthenticationProvider = customAuthenticationProvider;
        this.customLoginSuccessHandler = customLoginSuccessHandler;
        this.jwtRequestFilter = jwtRequestFilter;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder builder =
            http.getSharedObject(AuthenticationManagerBuilder.class);
        builder.authenticationProvider(customAuthenticationProvider);
        return builder.build();
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        //フォームログインを使うページ
        http
            .antMatcher("/**").csrf().disable()
            .authorizeRequests()
                .antMatchers("/authenticate").permitAll() 
                .antMatchers("/login", "/css/**", "/js/**").permitAll()
                .antMatchers("/api/admin/**").hasRole("ADMIN")
                .antMatchers("/api/**").authenticated()
                .antMatchers("/user/**").hasRole("USER")
                .anyRequest().authenticated()
            .and()
            .formLogin()
                .loginPage("/login")
                .successHandler(customLoginSuccessHandler)
            .and()
            .logout().permitAll()
            .and()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED); // ←フォームログイン（セッション有効）

        return http.build();
    }

    @Bean
    @Order(1) // JWT APIを優先
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {

        // APIはJWTで認証、完全Stateless
        http
            .antMatcher("/api/**").csrf().disable()
            .authorizeRequests()
                .antMatchers("/authenticate").permitAll()
                .antMatchers("/api/admin/**").hasRole("ADMIN")
                .antMatchers("/api/user/**").hasRole("USER")
                .anyRequest().authenticated()
            .and()
            .exceptionHandling().and()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // ←JWT（セッション無効）
            .and()
            .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


}
