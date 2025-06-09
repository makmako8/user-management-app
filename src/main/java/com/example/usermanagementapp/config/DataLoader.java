package com.example.usermanagementapp.config;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.usermanagementapp.entity.Role;
import com.example.usermanagementapp.entity.AppUser;
import com.example.usermanagementapp.repository.RoleRepository;
import com.example.usermanagementapp.repository.UserRepository;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
       
//        Role adminRole = roleRepository.findByRoleName("ROLE_ADMIN");
//        if (adminRole == null) {
//            adminRole = new Role();
//            adminRole.setRoleName("ROLE_ADMIN");
//            roleRepository.save(adminRole);
//        } else {
//            // ※再利用できるようにHibernate管理下のまま使用
//            adminRole = roleRepository.findByRoleName("ROLE_ADMIN");
//        }
//        // admin ユーザーが存在しないときだけ作成
//        if (userRepository.findByUsername("admin").isEmpty()) {
//            User admin = new User();
//            admin.setUsername("admin");
//            admin.setPassword(new BCryptPasswordEncoder().encode("adminpass"));
//            admin.setRoles(Set.of(adminRole));
//
//            userRepository.save(admin);
//            System.out.println(">>> 初期ユーザー admin/adminpass を登録しました");
//        }
    	
      Role adminRole = roleRepository.findByRoleName("ROLE_ADMIN");
     if (adminRole == null) {         adminRole = new Role();
          adminRole.setRoleName("ROLE_ADMIN");
          roleRepository.save(adminRole);
          } else {
        // ※再利用できるようにHibernate管理下のまま使用
          adminRole = roleRepository.findByRoleName("ROLE_ADMIN");
      }    // admin ユーザーが存在しないときだけ作成
      if (userRepository.findByUsername("admin").isEmpty()) {
         AppUser admin = new AppUser();
          admin.setUsername("admin");
         admin.setPassword(new BCryptPasswordEncoder().encode("adminpass"));
          admin.setRoles(Set.of(adminRole));

          userRepository.save(admin);
          System.out.println(">>> 初期ユーザー admin/adminpass を登録しました");
      }
     }
}
