package com.loan_sanction.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoanRequestDTO {

    private String applicantName;
    private int age;
    private String jobType;         // e.g. "Salaried", "Businessman", "Housewife"
    private boolean stableJob;      // true if job is stable (for salaried)
    private double monthlyIncome;   // monthly salary (for salaried) or base monthly income
    private double annualItr;       // optional, mainly for businessman / housewife
    private int creditScore;
    private double requestedAmount;
    private int tenureMonths;
}