package org.example.controller;

import org.example.entity.Fee;
import org.example.service.FeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/fee")
public class FeeController {
    private final FeeService feeService;

    public FeeController(FeeService feeService) {
        this.feeService = feeService;
    }

    @PostMapping(value = "/save-fee")
    public ResponseEntity<String> saveFee(@RequestBody String requestBody) {
        String s = feeService.saveFee(requestBody);
        if (s.equals("Saqlandi !!!")) {
            return ResponseEntity.ok(s);
        } return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(s);
    }

    @PutMapping("/update/{id}")
    public String updateFee(@RequestBody String feeSum,@PathVariable Long id){
        return feeService.updateFee(feeSum,id); 
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<String> deleteFee(@PathVariable Long id){
        return ResponseEntity.ok(feeService.deleteFee(id));
    }

    @GetMapping("")
    public String getAllFee(Model model){
        List<Fee> allFees = feeService.getAllFees();
        model.addAttribute("allFees",allFees);
        return "save-fee";
    }
}
