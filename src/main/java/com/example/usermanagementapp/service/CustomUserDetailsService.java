package com.example.usermanagementapp.service;


import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.usermanagementapp.entity.AppUser;
import com.example.usermanagementapp.entity.Role;
import com.example.usermanagementapp.repository.UserRepository;
@Service
public class CustomUserDetailsService implements UserDetailsService {
	

    private final UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
      }
    
    @Autowired
    public void setPasswordEncoder(@Lazy PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       
    	AppUser user = userRepository.findByUsername(username)
    			.orElseThrow(() -> new UsernameNotFoundException("ユーザーが見つかりません: " + username));
    
    	
    	 Set<Role> roles = user.getRoles();
    	    boolean isAdmin = roles.stream().anyMatch(r -> r.getRoleName().equals("ROLE_ADMIN"));
    	    boolean isUser = roles.stream().anyMatch(r -> r.getRoleName().equals("ROLE_USER"));

    	    System.out.println("✅ DBから取得したユーザー名: " + user.getUsername());
    	    System.out.println("🔑 DBから取得したパスワード: " + user.getPassword());
    	    System.out.println("🔍 ADMINロール: " + isAdmin);
    	    System.out.println("🔍 USERロール: " + isUser);
    	    user.getRoles().forEach(r -> System.out.println("→ " + r.getRoleName()));

  
    	    

    	    // 🔐 パスワード照合（ログ表示のみ）
   	    boolean matchAdmin = passwordEncoder.matches("adminpass", user.getPassword());
   	    boolean matchUser = passwordEncoder.matches("testpass7", user.getPassword());    	
   	    System.out.println("🧪 照合 adminpass = " + matchAdmin);
   	    System.out.println("🧪 照合 testpass7 = " + matchUser);
     
//    	  List<GrantedAuthority> authorities = roles.stream()

          List<GrantedAuthority> authorities = user.getRoles().stream()
        		    .map(role -> {
        		        System.out.println("🔎 role to authority: " + role.getRoleName());
        		        return new SimpleGrantedAuthority(role.getRoleName());
        		    })
        		    .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.isEnabled(),   // enabled
                true,               // accountNonExpired
                true,               // credentialsNonExpired
                true,               // accountNonLocked
                authorities
                
                
            );
        }
        
        
        
    
}


