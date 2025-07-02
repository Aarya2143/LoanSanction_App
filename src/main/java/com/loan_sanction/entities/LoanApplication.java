package com.loan_sanction.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
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

    @NotBlank(message = "Applicant name is required")
    private String applicantName;

    @NotNull(message = "Age is required")
    @Min(value = 21, message = "Age must be at least 21")
    @Max(value = 50, message = "Age must be at most 50")
    private Integer age;

    @NotBlank(message = "Job type is required")
    private String jobType;

    @NotNull(message = "Monthly income is required")
    @PositiveOrZero(message = "Monthly income must be zero or positive")
    private Double monthlyIncome;

    @NotNull(message = "Credit score is required")
    @Min(value = 600, message = "Credit score must be at least 600")
    @Max(value = 900, message = "Credit score must be at most 900")
    private Integer creditScore;

    @NotNull(message = "Requested amount is required")
    @DecimalMin(value = "50000.0", message = "Requested amount must be at least 50,000")
    @DecimalMax(value = "4000000.0", message = "Requested amount must not exceed 40,00,000")
    private Double requestedAmount;

    @NotNull(message = "Tenure months is required")
    @Min(value = 3, message = "Tenure must be at least 3 months")
    @Max(value = 60, message = "Tenure must not exceed 60 months")
    private Integer tenureMonths;

    private Double sanctionedAmount;   // this give from backend

}
