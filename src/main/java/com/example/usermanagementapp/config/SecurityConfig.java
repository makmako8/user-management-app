package com.example.usermanagementapp.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Lazy
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private CustomLoginSuccessHandler customLoginSuccessHandler;
    @Bean
    public PasswordEncoder passwordEncoder() {
    	  return new BCryptPasswordEncoder(); 
    }
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService) // ← 認証に使うサービスを登録
            .passwordEncoder(passwordEncoder());
    }
    @Bean
    public AuthenticationSuccessHandler customSuccessHandler() {
        return new CustomLoginSuccessHandler();
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/register","/login", "/css/**", "/js/**","/h2-console/**").permitAll()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/user/**").hasRole("USER")
                .anyRequest().authenticated()
            .and()
                .formLogin()
                .loginPage("/login")
                .successHandler(customLoginSuccessHandler)
            .and()
                .logout()
                .permitAll();

        // H2コンソール用の設定
        http.csrf().disable();
        http.headers().frameOptions().disable();
    }



    

}
