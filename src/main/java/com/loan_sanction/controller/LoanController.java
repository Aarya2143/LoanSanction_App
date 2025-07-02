package com.loan_sanction.controller;

import com.loan_sanction.dto.LoanRequestDTO;
import com.loan_sanction.entities.LoanApplication;
import com.loan_sanction.service.LoanService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/loans")
public class LoanController {

    private final LoanService service;

    public LoanController(LoanService service) {
        this.service = service;
    }

    @PostMapping("/apply")
    public ResponseEntity<?> applyForLoan(@RequestBody LoanRequestDTO dto) {
        LoanApplication loan = service.processLoan(dto);
        return ResponseEntity.ok(Map.of(
                "message", "Loan sanctioned",
                "sanctionedAmount", loan.getSanctionedAmount()
        ));
    }

    @PostMapping("/max-eligibility")
    public ResponseEntity<?> checkMaxEligibility(@RequestBody LoanRequestDTO dto) {
        double maxLoan = service.calculateMaxLoan(dto);
        return ResponseEntity.ok(Map.of(
                "message", "Your max loan eligibility",
                "maxEligibleAmount", maxLoan
        ));
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<?> getLoanDetails(@PathVariable Long id) {
        LoanApplication loan = service.getLoanDetails(id);
        return ResponseEntity.ok(loan);
    }
}
