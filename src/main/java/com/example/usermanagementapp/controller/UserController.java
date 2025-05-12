package com.example.usermanagementapp.controller;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.usermanagementapp.entity.User;
import com.example.usermanagementapp.service.UserService;


@Controller
public class UserController {
	

    @Autowired
    private UserService userService;
    
	@PostMapping("/login")
	public String login(@RequestParam String username, @RequestParam String password, Model model) {
	    if ("admin".equals(username) && "pass".equals(password)) {
	        return "redirect:/home";
	    } else {
	        model.addAttribute("error", "ログイン失敗");
	        return "login";
	    }
	}
    // ユーザー登録フォーム
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    // ユーザー登録処理
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user")User user, Model model) {
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            user.setRoles("USER");
        }
        try {
        	System.out.println("登録情報: ユーザー名 = " + user.getUsername() + ", パスワード = " + user.getPassword());
            userService.registerUser(user);
        } catch (IllegalArgumentException e) {
            // ユーザー名が既に存在する場合のエラーメッセージ
            model.addAttribute("errorMessage", e.getMessage());
            return "register"; // エラーメッセージを表示
        }catch (Exception e) {
            // その他のエラー
            model.addAttribute("errorMessage", "登録処理に失敗しました。");
            e.printStackTrace();  // エラーの詳細を表示
            return "register";
    	// 入力された情報をログに出力して確認
        }
        return "redirect:/login"; // 登録後にログインページにリダイレクト
    }

}
