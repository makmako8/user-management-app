package com.example.usermanagementapp.controller.api;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.usermanagementapp.dto.UserRequestDTO;
import com.example.usermanagementapp.entity.AppUser;
import com.example.usermanagementapp.repository.UserRepository;
import com.example.usermanagementapp.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserApiController {

    @Autowired
    private UserRepository userRepository;
    
//    @PreAuthorize("hasRole('ADMIN')") // 管理者だけが使えるように以下を追加することもできます
    @Autowired
    private UserService userService;
    


    @GetMapping
    public List<AppUser> getAllUsers() {
         return userService.findAllUsers();
    }
    // 単一ユーザーをID指定で取得（オプション機能）
    @GetMapping("/{id}")
    public AppUser getUserById(@PathVariable Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("指定されたユーザーが見つかりません: ID = " + id));
    }
    // 新規ユーザー登録API（POST）
    @PostMapping
    public String registerUserDTO(@RequestBody UserRequestDTO request) {
        try {
            userService.registerUserDTO(
                request.getUsername(),
                request.getPassword(),
                request.getRoleName() // ✅ OK！
            );
            return "ユーザー登録に成功しました";
        } catch (IllegalArgumentException e) {
            return "登録失敗: " + e.getMessage();
        }
    }
}