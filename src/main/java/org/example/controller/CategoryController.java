package org.example.controller;

import org.example.entity.Fee;
import org.example.entity.RuleCategory;
import org.example.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("api/category")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    @PostMapping("/save")
    public ResponseEntity<String> saveCategory(@RequestBody String ruleCategory){
        String result = categoryService.saveCategory(ruleCategory);
        if (result.equals("Saqlandi !!!")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id){
        return ResponseEntity.ok(categoryService.deleteCategory(id));
    }

    @PutMapping("/update")
    public String updateCategory(@RequestBody RuleCategory category) {
        return categoryService.updateCategory(category);
    }
    @GetMapping("")
    public String getAllCategories(Model model){
        List<RuleCategory> allCategories = categoryService.getAllCategories();
        model.addAttribute("allCategories",allCategories);
        return "save-category";
    }
}
