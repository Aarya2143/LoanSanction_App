package com.loan_sanction.util;

import com.loan_sanction.dto.LoanRequestDTO;
import com.loan_sanction.entities.LoanApplication;

public class TestConstants {

    public static final String APPLICANT_NAME = "John";
    public static final int APPLICANT_AGE = 30;
    public static final String EMPLOYMENT_TYPE = "Salaried";
    public static final boolean IS_CIBIL_OK = true;
    public static final double MONTHLY_INCOME = 50000;
    public static final double EXISTING_EMI = 0;
    public static final int CIBIL_SCORE = 750;
    public static final double REQUESTED_AMOUNT = 600000;
    public static final int TENURE_MONTHS = 24;
    public static final double SANCTIONED_AMOUNT = 600000.0;
    public static final long LOAN_ID = 1L;
    public static final double MAX_ELIGIBLE_AMOUNT = 1000000.0;

    public static final long INVALID_LOAN_ID = 99L;

    public static final String ERROR_SERVICE = "Service error";
    public static final String ERROR_ELIGIBILITY = "Eligibility error";
    public static final String ERROR_NOT_FOUND = "Loan not found";

    public static LoanRequestDTO getSampleRequestDTO() {
        return new LoanRequestDTO(
                APPLICANT_NAME,
                APPLICANT_AGE,
                EMPLOYMENT_TYPE,
                IS_CIBIL_OK,
                MONTHLY_INCOME,
                EXISTING_EMI,
                CIBIL_SCORE,
                REQUESTED_AMOUNT,
                TENURE_MONTHS
        );
    }

    public static LoanApplication getSampleLoanApplication() {
        return LoanApplication.builder()
                .id(LOAN_ID)
                .applicantName(APPLICANT_NAME)
                .sanctionedAmount(SANCTIONED_AMOUNT)
                .build();
    }

    public static final LoanRequestDTO TEST_DTO_VALID = new LoanRequestDTO(
            "Alice", 30, "Salaried", true, 50000, 0, 750, 200000, 12);

    public static final LoanRequestDTO TEST_DTO_AMOUNT_TOO_LOW = new LoanRequestDTO(
            "Bob", 30, "Salaried", true, 50000, 0, 700, 40000, 12);

    public static final LoanRequestDTO TEST_DTO_AMOUNT_TOO_HIGH = new LoanRequestDTO(
            "Bob", 30, "Salaried", true, 50000, 0, 700, 5000000, 12);

    public static final LoanRequestDTO TEST_DTO_EXCEEDS_ELIGIBLE = new LoanRequestDTO(
            "Charlie", 30, "Salaried", true, 50000, 0, 700, 4000000, 12);

    public static final LoanRequestDTO TEST_DTO_FOIR_FAIL = new LoanRequestDTO(
            "Dave", 30, "Salaried", true, 20000, 0, 750, 500000, 12);

    public static final LoanRequestDTO TEST_DTO_TENURE_AGE_FAIL = new LoanRequestDTO(
            "Eve", 49, "Salaried", true, 50000, 0, 700, 100000, 24);

}
