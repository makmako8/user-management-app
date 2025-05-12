package com.example.usermanagementapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
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

//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user = userRepository.findByUsername(username)
//            .orElseThrow(() -> new UsernameNotFoundException("ユーザーが見つかりません"));
////         データベースからユーザー情報を取得
////        User user = userRepository.findByUsername(username);
////        if (user == null) {
////            throw new UsernameNotFoundException("User not found: " + username);
////        }
//z
//        return new org.springframework.security.core.userdetails.User(
//            user.getUsername(),
//            user.getPassword(),
//            Collections.singleton(() -> "ROLE_" + user.getRole())
//        );
//    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // データベースからユーザー情報を取得
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        // パスワードのエンコードが必要な場合、エンコードされたパスワードを返す
        return new org.springframework.security.core.userdetails.User(
            user.getUsername(),
            user.getPassword(),
            AuthorityUtils.createAuthorityList(user.getRole())
        );
    }
}