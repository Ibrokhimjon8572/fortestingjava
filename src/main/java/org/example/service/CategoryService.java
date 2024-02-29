package org.example.service;

import org.example.entity.Rule;
import org.example.entity.RuleCategory;
import org.example.repository.RuleCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    private final RuleCategoryRepository ruleCategoryRepository;

    public CategoryService(RuleCategoryRepository ruleCategoryRepository) {
        this.ruleCategoryRepository = ruleCategoryRepository;
    }

    public String saveCategory(String category){
        RuleCategory ruleCategory = new RuleCategory();
        Boolean b = ruleCategoryRepository.existsByTitle(category);
        if (!b){
            ruleCategory.setTitle(category);
            ruleCategoryRepository.save(ruleCategory);
            return "Saqlandi !!!";
        }else return "Bunday Kategoriya bor";
    }

    public String deleteCategory(Long id){
        Optional<RuleCategory> byId = ruleCategoryRepository.findById(id);
        if (byId.isPresent()){
            RuleCategory ruleCategory = byId.get();

            for (Rule rule : ruleCategory.getRules()){
                rule.getCategories().remove(ruleCategory);
            }
            ruleCategoryRepository.deleteById(id);
            return "O'chirildi";
        } else {
            return "Xatoilik.";
        }
    }

    public String updateCategory(RuleCategory updatedCategory) {
        Optional<RuleCategory> optionalCategory = ruleCategoryRepository.findById(updatedCategory.getId());

        if (optionalCategory.isPresent()) {
            RuleCategory existingCategory = optionalCategory.get();
            if (updatedCategory.getTitle() != null) {
                existingCategory.setTitle(updatedCategory.getTitle());
            }
            ruleCategoryRepository.save(existingCategory);
            return "O'zgartirildi";
        } else {
            return "Bunday Kategoriya yo'q";
        }
    }

    public List<RuleCategory> findAll() {
        return ruleCategoryRepository.findAll();
    }

    public List<RuleCategory> getAllCategories() {
        List<RuleCategory> all = ruleCategoryRepository.findAll();
        return all;
    }
}
