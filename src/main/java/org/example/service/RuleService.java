package org.example.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.entity.Fee;
import org.example.entity.Rule;
import org.example.entity.RuleCategory;
import org.example.repository.FeeRepository;
import org.example.repository.RuleCategoryRepository;
import org.example.repository.RuleRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RuleService {
    private final RuleRepository ruleRepository;
    private final FeeRepository feeRepository;
    private final RuleCategoryRepository ruleCategoryRepository;

    public RuleService(RuleRepository ruleRepository, FeeRepository feeRepository, RuleCategoryRepository ruleCategoryRepository) {
        this.ruleRepository = ruleRepository;
        this.feeRepository = feeRepository;
        this.ruleCategoryRepository = ruleCategoryRepository;
    }

    public String saveRule(Rule rule) {


        if (rule != null) {
            Rule ruleToSave = new Rule();


            ruleToSave.setContent(rule.getContent());
            ruleToSave.setCreatedDate(rule.getCreatedDate());

            List<Fee> existingFees = rule.getFees().stream()
                    .map(fee -> feeRepository.findById(fee.getId()).orElse(null))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            ruleToSave.setFees(existingFees);

            List<RuleCategory> existingCategories = rule.getCategories().stream()
                    .map(category -> ruleCategoryRepository.findById(category.getId()).orElse(null))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            ruleToSave.setCategories(existingCategories);

            ruleRepository.save(ruleToSave);
            return "Saqlandi";
        } else {
            return "Xatolik yuz berdi";
        }
    }


    public String deleteRules(Long id) {
        Rule rule = ruleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Rule not found with id: " + id));
        rule.getCategories().clear();
        rule.getFees().clear();
        ruleRepository.save(rule);

        ruleRepository.delete(rule);
        return "O'chirildi";
    }

    public String updateRule(Rule updatedRule) {
        Optional<Rule> optionalRule = ruleRepository.findById(updatedRule.getId());

        Rule rule = optionalRule.get();

        rule.setCreatedDate(updatedRule.getCreatedDate());
        rule.setFees(updatedRule.getFees());
        rule.setContent(updatedRule.getContent());

        ruleRepository.save(rule);
        return "Some";

    }

    public Map<String, List<Object>> search(String keyword) {
        Map<String, List<Object>> results = new HashMap<>();

        List<Rule> allRules = ruleRepository.findAll();

        results.put("rule", filterByEqualKeyword(allRules, keyword));

        return results;
    }

    private <T> List<Object> filterByEqualKeyword(List<T> records, String keyword) {
        return records.stream()
                .filter(record -> isEqualKeyword(record, keyword))
                .collect(Collectors.toList());
    }

    private <T> boolean isEqualKeyword(T record, String keyword) {
        if (record instanceof Rule) {
            Rule rule = (Rule) record;
            return rule.getContent().toLowerCase().contains(keyword.toLowerCase());
        } else if (record instanceof RuleCategory) {
            RuleCategory ruleCategory = (RuleCategory) record;
            return ruleCategory.getTitle().toLowerCase().contains(keyword.toLowerCase());
        } else if (record instanceof Fee) {
            Fee fee = (Fee) record;
            return fee.getFeeSum().toLowerCase().contains(keyword.toLowerCase());
        } else {
            return false;
        }
    }
    public Map<String, List<Object>> getCategoryAndFee() {
        Map<String, List<Object>> result = new HashMap<>();

        List<RuleCategory> ruleCategories = ruleCategoryRepository.findAll();
        result.put("rule_category", Collections.unmodifiableList(ruleCategories));

        List<Fee> fees = feeRepository.findAll();
        result.put("fee", Collections.unmodifiableList(fees));

        return result;
    }
    public List<Rule> getRuleByCategoryId(Long id) {
        List<Rule> rules = ruleRepository.findByCategoriesId(id);
        return rules;
    }
    public Rule getRuleById(Long id) {
        Optional<Rule> rule = ruleRepository.findById(id);
        Rule rule1 = rule.get();
        return rule1;
    }

    public List<RuleCategory> getAllCategory() {
        return ruleCategoryRepository.findAll();
    }

    public String  removeFeeFromRule(Long ruleId, Long feeId) {
        Optional<Rule> optionalRule = ruleRepository.findById(ruleId);
        Optional<Fee> optionalFee = feeRepository.findById(feeId);

        if (optionalRule.isPresent() && optionalFee.isPresent()) {
            Rule rule = optionalRule.get();
            Fee fee = optionalFee.get();

            rule.getFees().remove(fee);
            ruleRepository.save(rule);
            return "O'chirildi";
        } else {
            return "Xatolik yuz berdi";
        }
    }

    public List<Fee> getAllFeeS() {
        return feeRepository.findAll();
    }

    public String updateRulesFee(List<Long> feeIds, Long ruleId) {
        Optional<Rule> optionalRule = ruleRepository.findById(ruleId);
        if (optionalRule.isPresent()) {
            Rule rule = optionalRule.get();
            List<Fee> ruleFees = rule.getFees();

            Set<Long> ruleFeeIds = new HashSet<>();
            for (Fee fee : ruleFees) {
                ruleFeeIds.add(fee.getId());
            }

            // Iterate over the given fee IDs
            for (Long feeId : feeIds) {
                Optional<Fee> optionalFee = feeRepository.findById(feeId);
                if (optionalFee.isPresent()) {
                    Fee fee = optionalFee.get();
                    if (!ruleFeeIds.contains(feeId)) {
                        ruleFees.add(fee);
                    }
                } else {
                    return "Fee with ID " + feeId + " not found.";
                }
            }
            rule.setFees(ruleFees);
            ruleRepository.save(rule);

            return "Rule fees updated successfully.";
        } else {
            return "Rule with ID " + ruleId + " not found.";
        }
    }
}
