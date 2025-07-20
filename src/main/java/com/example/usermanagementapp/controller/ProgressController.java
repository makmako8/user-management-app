package com.example.usermanagementapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
    public String showProgress(Model model) {
        model.addAttribute("month1Items", progressRepo.findByCategory("1ヶ月目"));
        model.addAttribute("month2Items", progressRepo.findByCategory("2ヶ月目"));
        model.addAttribute("month3Items", progressRepo.findByCategory("3ヶ月目"));
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
}
