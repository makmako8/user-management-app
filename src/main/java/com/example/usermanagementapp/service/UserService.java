package com.example.usermanagementapp.service;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.usermanagementapp.entity.AppUser;
import com.example.usermanagementapp.entity.Role;
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
		  Optional<Role> optionalRole = roleRepository.findByRoleName(roleName);
		  Role role;
		  if (optionalRole.isPresent()) {
			    role = optionalRole.get();
			} else {
			    role = new Role();
			    role.setRoleName(roleName);
			    role = roleRepository.save(role);
			}
	       AppUser user = new AppUser();
	       user.setUsername(username);
	       user.setPassword(passwordEncoder.encode(rawPassword));
	       user.setRoles(Set.of(role));

        // パスワードをエンコード
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // ロールをセット（Setで渡す）
        user.setRoles(Set.of(role));
        // ユーザーを保存
        userRepository.save(user);
    }
    public void registerUserDTO(String username, String rawPassword, String roleName) {
        // ユーザー名の重複チェック
	     if (userRepository.findByUsername(username).isPresent()) {
	         throw new IllegalArgumentException("ユーザー名は既に存在します");
	     }

	// パスワードのエンコード
		  Optional<Role> optionalRole = roleRepository.findByRoleName(roleName);
		  Role role;
		  if (optionalRole.isPresent()) {
		      role = optionalRole.get();
		  } else {
		      role = new Role();
		      role.setRoleName(roleName);
		      role = roleRepository.save(role);
		  }
	       AppUser user = new AppUser();
	       user.setUsername(username);
	       user.setPassword(passwordEncoder.encode(rawPassword));
	       user.setRoles(Set.of(role));

        // パスワードをエンコード
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // ロールをセット（Setで渡す）
        user.setRoles(Set.of(role));
        // ユーザーを保存
        userRepository.save(user);
    }
    public void registerUserWithRole(AppUser user, String roleName) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalArgumentException("ユーザー名は既に存在します");
        }


        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);

        Optional<Role> optionalRole = roleRepository.findByRoleName(roleName);
        Role role;
        if (optionalRole.isPresent()) {
            role = optionalRole.get();
        } else {
            role = new Role();
            role.setRoleName(roleName);
            role = roleRepository.save(role);
        }
        user.setRoles(Set.of(role));
        userRepository.save(user);
    }
    
    public List<AppUser> findAllUsers() {
        return userRepository.findAll();
    }
    public AppUser getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("ユーザーが見つかりません: " + username));
    }

}
