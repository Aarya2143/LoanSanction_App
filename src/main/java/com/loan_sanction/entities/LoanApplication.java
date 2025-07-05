package com.loan_sanction.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class LoanApplication {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String applicantName;
    private Integer age;
    private String jobType;
    private Double monthlyIncome;
    private Integer creditScore;
    private Double requestedAmount;
    private Integer tenureMonths;
    private Double sanctionedAmount;   // This calculated from backend
}
