package com.loan_sanction.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoanRequestDTO {

    @NotBlank(message = "Applicant name is required")
    private String applicantName;

    @Min(value = 21, message = "Age must be at least 21")
    @Max(value = 50, message = "Age must not exceed 50")
    private int age;

    @NotBlank(message = "Job type is required")
    private String jobType;  // e.g. Salaried, Businessman

    private boolean stableJob;

    @PositiveOrZero(message = "Monthly income must be zero or positive")
    private double monthlyIncome;

    @PositiveOrZero(message = "Annual ITR must be zero or positive")
    private double annualItr;

    @Min(value = 300, message = "Credit score must be at least 300")
    @Max(value = 900, message = "Credit score must not exceed 900")
    private int creditScore;

    @Positive(message = "Requested amount must be positive")
    private double requestedAmount;

    @Min(value = 3, message = "Tenure must be at least 3 months")
    @Max(value = 60, message = "Tenure must not exceed 60 months")
    private int tenureMonths;
}