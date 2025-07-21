package com.example.usermanagementapp.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.usermanagementapp.entity.ProgressItem;
import com.example.usermanagementapp.repository.ProgressItemRepository;

@Controller
@RequestMapping("/user")
public class ProgressController {

    @Autowired
    private ProgressItemRepository progressRepo;

    @GetMapping("/progress")
    public String showProgress(Model model, Principal principal) {
    	String username = principal.getName();
        model.addAttribute("month1Items", progressRepo.findByCategoryAndUsername("1ヶ月目", username));
        model.addAttribute("month2Items", progressRepo.findByCategoryAndUsername("2ヶ月目", username));
        model.addAttribute("month3Items", progressRepo.findByCategoryAndUsername("3ヶ月目", username));
        return "user/progress";
    }

    @PostMapping("/update")
    public String updateProgress(@RequestParam Long id,
    							 @RequestParam(required = false, defaultValue = "false") boolean completed) {
        ProgressItem item = progressRepo.findById(id).orElseThrow();
        item.setCompleted(completed);
        progressRepo.save(item);
        return "redirect:/user/progress";
    }
    
    // 追加：管理者がユーザーに進捗を割り当てるフォーム用の表示・保存処理
    @GetMapping("/assign-progress")
    public String assignProgressForm(Model model) {
        model.addAttribute("progressItem", new ProgressItem());
        return "user/assign-progress";
    }

    @PostMapping("/assign-progress")
    public String assignProgress(@ModelAttribute ProgressItem progressItem) {
        progressItem.setCompleted(false); // 初期状態は未完了
        progressRepo.save(progressItem);
        return "redirect:/user/progress";
    }
}
