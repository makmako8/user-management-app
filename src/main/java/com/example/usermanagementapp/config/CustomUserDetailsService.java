package com.example.usermanagementapp.config;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.usermanagementapp.entity.User;
import com.example.usermanagementapp.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
	@Autowired
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       
    	User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("ユーザーが見つかりません:  " + username);
        }
        System.out.println("✅ DBから取得したユーザー名: " + user.getUsername());
        System.out.println("🔑 DBから取得したパスワード: " + user.getPassword());

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_USER")) // 権限は適当に仮設定OK
                
                
            );
        
        
   }
    
    
}


