package com.example.usermanagementapp.controller;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.usermanagementapp.entity.AppUser;
import com.example.usermanagementapp.entity.Role;
import com.example.usermanagementapp.repository.RoleRepository;
import com.example.usermanagementapp.repository.UserRepository;


@Controller
@RequestMapping("/admin/system-settings")
@PreAuthorize("hasRole('ADMIN')")
public class SystemSettingsController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @GetMapping
    public String showSystemSettings(Model model) {
        List<AppUser> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "admin/system-settings";
    }

    @PostMapping("/change-role")
    public String changeUserRole(@RequestParam Long userId,
                                 @RequestParam String role,
                                 RedirectAttributes redirectAttributes) {
        Optional<AppUser> optionalUser = userRepository.findById(userId);
        Optional<Role> newRole = roleRepository.findByRoleName(role);

        if (optionalUser.isPresent() && newRole.isPresent()) {
            AppUser user = optionalUser.get();
            Set<Role> roles = new HashSet<>();
            roles.add(newRole.get());
            user.setRoles(roles);
            userRepository.save(user);
            redirectAttributes.addFlashAttribute("success", user.getUsername() + " のロールを " + role + " に変更しました。");
        } else {
            redirectAttributes.addFlashAttribute("error", "ユーザーまたはロールが見つかりませんでした。");
        }

        return "redirect:/admin/system-settings";
    }
}
