package com.loan_sanction.util;

public class LoanConstants {

    private LoanConstants() {

    }
    // Cross-origin
    public static final String CROSS_ORIGIN_URL = "http://localhost:3000";

    // Response messages
    public static final String MSG_LOAN_SANCTIONED = "Loan sanctioned";
    public static final String MSG_MAX_ELIGIBILITY = "Your max loan eligibility";
    public static final String MSG_LOAN_AMOUNT_LIMIT = "Loan amount must be between ₹50,000 and ₹40,00,000";
    public static final String MSG_EXCEEDS_MAX_ELIGIBILITY = "Exceeds your maximum eligible loan: ₹%s";
    public static final String MSG_EMI_EXCEEDS_LIMIT = "EMI exceeds 50% of monthly income.";
    public static final String MSG_TENURE_AGE_LIMIT = "Loan tenure exceeds age 50 limit.";
    public static final String MSG_LOAN_NOT_FOUND = "Loan application not found for ID: %s";

    // Response keys
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_SANCTIONED_AMOUNT = "sanctionedAmount";
    public static final String KEY_MAX_ELIGIBLE_AMOUNT = "maxEligibleAmount";

    // Limits
    public static final double MIN_LOAN_AMOUNT = 50000;
    public static final double MAX_LOAN_AMOUNT = 4000000;
}
