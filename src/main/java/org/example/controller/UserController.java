package org.example.controller;

import org.example.entity.Rule;
import org.example.entity.RuleCategory;
import org.example.service.RuleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("")
public class UserController {
    private final RuleService ruleService;

    public UserController(RuleService ruleService) {
        this.ruleService = ruleService;
    }

    @GetMapping("")
    public String getAllCategory(Model model){
        List<RuleCategory> allCategory = ruleService.getAllCategory();
        model.addAttribute("allCategory",allCategory);
        return "user-index";
    }
    @GetMapping("/api/search")
    public String search(@RequestParam String keyword, Model model){
        Map<String, List<Object>> results = ruleService.search(keyword);
        model.addAttribute("results",results);
        return"search-user";
    }
    @GetMapping("/{id}")
    public String getByCategoryId(@PathVariable Long id, Model model){
        List<Rule> ruleByCategoryId = ruleService.getRuleByCategoryId(id);
        model.addAttribute("ruleByCategoryId",ruleByCategoryId);
        return "user-rule";
    }
}
