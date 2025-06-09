package com.example.usermanagementapp.service;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.usermanagementapp.entity.Role;
import com.example.usermanagementapp.entity.AppUser;
import com.example.usermanagementapp.repository.RoleRepository;
import com.example.usermanagementapp.repository.UserRepository;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;



    public void registerUser(String username, String rawPassword, String roleName) {
        // ユーザー名の重複チェック
	     if (userRepository.findByUsername(username).isPresent()) {
	         throw new IllegalArgumentException("ユーザー名は既に存在します");
	     }

	// パスワードのエンコード
		  Role userRole = roleRepository.findByRoleName(roleName);
	      if (userRole == null) {
	           userRole = new Role();
	           userRole.setRoleName("ROLE_USER");
	           userRole = roleRepository.save(userRole); // ← saveして、永続化されたエンティティを使う
	      }
	       AppUser user = new AppUser();
	       user.setUsername(username);
	       user.setPassword(passwordEncoder.encode(rawPassword));
	       user.setRoles(Set.of(userRole));

        // パスワードをエンコード
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // ロールをセット（Setで渡す）
        user.setRoles(Set.of(userRole));
        // ユーザーを保存
        userRepository.save(user);
    }
    public void registerUserDTO(String username, String rawPassword, String roleName) {
        // ユーザー名の重複チェック
	     if (userRepository.findByUsername(username).isPresent()) {
	         throw new IllegalArgumentException("ユーザー名は既に存在します");
	     }

	// パスワードのエンコード
		  Role userRole = roleRepository.findByRoleName(roleName);
	      if (userRole == null) {
	           userRole = new Role();
	           userRole.setRoleName("ROLE_USER");
	           userRole = roleRepository.save(userRole); // ← saveして、永続化されたエンティティを使う
	      }
	       AppUser user = new AppUser();
	       user.setUsername(username);
	       user.setPassword(passwordEncoder.encode(rawPassword));
	       user.setRoles(Set.of(userRole));

        // パスワードをエンコード
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // ロールをセット（Setで渡す）
        user.setRoles(Set.of(userRole));
        // ユーザーを保存
        userRepository.save(user);
    }
    public void registerUserWithRole(AppUser user, String roleName) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalArgumentException("ユーザー名は既に存在します");
        }


        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);

        Role role = roleRepository.findByRoleName(roleName);
        if (role == null) {
            role = new Role();
            role.setRoleName(roleName);
            roleRepository.save(role);
        }
        user.setRoles(Set.of(role));
        userRepository.save(user);
    }
    
    public List<AppUser> findAllUsers() {
        return userRepository.findAll();
    }

}
