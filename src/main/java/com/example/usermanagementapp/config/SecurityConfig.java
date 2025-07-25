package com.example.usermanagementapp.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
        http
        	.csrf().disable()
            .headers().frameOptions().disable().and()
            .authorizeRequests()
                .antMatchers("/register", "/login", "/css/**", "/js/**", "/h2-console/**", "/authenticate").permitAll()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/user/**").hasRole("USER")
                .anyRequest().authenticated()
            .and()
                .formLogin()
                .loginPage("/login")
                .successHandler(customLoginSuccessHandler)
            .and()
                .logout()
                .permitAll()
            .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);


        http.authenticationProvider(customAuthenticationProvider);
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
