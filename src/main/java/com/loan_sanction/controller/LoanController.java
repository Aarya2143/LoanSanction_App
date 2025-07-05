package com.loan_sanction.controller;

import com.loan_sanction.dto.LoanRequestDTO;
import com.loan_sanction.entities.LoanApplication;
import com.loan_sanction.service.LoanService;
import com.loan_sanction.util.LoanConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@CrossOrigin(origins = LoanConstants.CROSS_ORIGIN_URL)@RestController
@RequestMapping("/api/loans")
public class LoanController {

    @Autowired
    private LoanService service;

    @PostMapping("/apply")
    public ResponseEntity<Map<String, Object>> applyForLoan(@RequestBody LoanRequestDTO dto) {
        LoanApplication loan = service.processLoan(dto);
        return ResponseEntity.ok(Map.of(
                LoanConstants.KEY_MESSAGE, LoanConstants.MSG_LOAN_SANCTIONED,
                LoanConstants.KEY_SANCTIONED_AMOUNT, loan.getSanctionedAmount()
        ));
    }

    @PostMapping("/max-eligibility")
    public ResponseEntity<Map<String, Object>> checkMaxEligibility(@RequestBody LoanRequestDTO dto) {
        double maxLoan = service.calculateMaxLoan(dto);
        return ResponseEntity.ok(Map.of(
                LoanConstants.KEY_MESSAGE, LoanConstants.MSG_MAX_ELIGIBILITY,
                LoanConstants.KEY_MAX_ELIGIBLE_AMOUNT, maxLoan
        ));
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<LoanApplication> getLoanDetails(@PathVariable Long id) {
        LoanApplication loan = service.getLoanDetails(id);
        return ResponseEntity.ok(loan);
    }
}
