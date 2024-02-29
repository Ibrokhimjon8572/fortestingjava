package org.example.controller;

import org.example.entity.Fee;
import org.example.entity.Rule;
import org.example.entity.RuleCategory;
import org.example.service.RuleService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("api/rule")
public class RuleController {
    private final RuleService ruleService;

    public RuleController(RuleService ruleService) {
        this.ruleService = ruleService;
    }

    @GetMapping
    public String getCategoryAndFee(Model model){
        Map<String ,List<Object>> result = ruleService.getCategoryAndFee();
        model.addAttribute("result", result);
        return "save-rule";
    }

    @PostMapping("/save-rule")
    public ResponseEntity<String> saveRule(@RequestBody Rule rule){
        return ResponseEntity.ok(ruleService.saveRule(rule));
    }

    @GetMapping("/search")
    public String search(@RequestParam String keyword,Model model){
        Map<String, List<Object>> results = ruleService.search(keyword);
        model.addAttribute("results",results);
        return"search-results";
    }
    @GetMapping("/category")
    public String getAllCategory(Model model){
        List<RuleCategory> allCategory = ruleService.getAllCategory();
        model.addAttribute("allCategory",allCategory);
        return "index";
    }

    @GetMapping("/category/{id}")
    public String getByCategoryId(@PathVariable Long id,Model model){
        List<Rule> ruleByCategoryId = ruleService.getRuleByCategoryId(id);
        model.addAttribute("ruleByCategoryId",ruleByCategoryId);
        return "rulebycategory";
    }

    @GetMapping("/{id}")
    public Rule getRuleBYId(@PathVariable Long id){
        return ruleService.getRuleById(id);
    }

    @PutMapping("/update-rule")
    public ResponseEntity<String> updateRule(@RequestBody Rule rule){

        return ResponseEntity.ok(ruleService.updateRule(rule));
    }

    @DeleteMapping("/category/delete/{id}")
    public ResponseEntity<String> deleteRule(@PathVariable("id") Long id){
        return ResponseEntity.ok(ruleService.deleteRules(id));
    }

    @DeleteMapping("/deletefee/{feeid}")
    public String deleteFeeInRule(@PathVariable Long feeid,@RequestBody Long ruleId){
        return ruleService.removeFeeFromRule(ruleId,feeid);
    }

    @GetMapping("/getfees")
    public List<Fee> getAllFee(){
        return ruleService.getAllFeeS();
    }

    @PostMapping("/savefeerule/{ruleId}")
    public String saveFeeRule(@PathVariable Long ruleId,@RequestBody List<Long> id){
        return ruleService.updateRulesFee(id,ruleId);
    }

    @GetMapping("/edit")
    public String showEditRule(Model model,@RequestParam Long id){
        Rule ruleById = ruleService.getRuleById(id);
        List<Fee> allFees = ruleService.getAllFeeS();

        Set<Long> associatedFeeIds = ruleById.getFees().stream()
                .map(Fee::getId)
                .collect(Collectors.toSet());

        Map<Long, Boolean> feeCheckMap = allFees.stream()
                .collect(Collectors.toMap(Fee::getId, fee -> associatedFeeIds.contains(fee.getId())));

        model.addAttribute("ruleById", ruleById);
        model.addAttribute("allFees", allFees);
        model.addAttribute("feeCheckMap", feeCheckMap);

        return "edit-rule";
    }
}
