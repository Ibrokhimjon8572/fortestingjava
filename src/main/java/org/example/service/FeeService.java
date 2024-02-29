package org.example.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.entity.Fee;
import org.example.entity.Rule;
import org.example.repository.FeeRepository;
import org.example.repository.RuleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FeeService {
    private final FeeRepository feeRepository;
    private final RuleRepository ruleRepository;


    public FeeService(FeeRepository feeRepository, RuleRepository ruleRepository) {
        this.feeRepository = feeRepository;
        this.ruleRepository = ruleRepository;
    }

    public String saveFee(String fee){
        Boolean b = feeRepository.existsByFeeSum(fee);
        if (!b) {
            Fee feeSave = new Fee();
            feeSave.setFeeSum(fee);
            feeRepository.save(feeSave);
            return "Saqlandi !!!";
        } return "Bunday Jarima mavjut";
    }

    public String deleteFee(Long feeId) {
        Optional<Fee> optionalFee = feeRepository.findById(feeId);
        if (optionalFee.isPresent()) {
            Fee fee = optionalFee.get();

            for (Rule rule : fee.getRules()) {
                rule.getFees().remove(fee);
            }
            feeRepository.deleteById(feeId);
            return "O'chirildi";
        } else {
            return "Xatoilik.";
        }
    }

    public String updateFee(String fee,Long id){
        Boolean b = feeRepository.existsByFeeSum(fee);
        Optional<Fee> byId = feeRepository.findById(id);
        Fee fee1 = byId.get();
        if (!b){
            fee1.setFeeSum(fee);
            feeRepository.save(fee1);
            return "Saqlandi !!";
        }else return "Xatolik yuz berdi";
    }

    public List<Fee> getAllFees() {
        return feeRepository.findAll();
    }
}
