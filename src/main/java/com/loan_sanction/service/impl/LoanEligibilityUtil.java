package com.loan_sanction.service.impl;

import com.loan_sanction.dto.LoanRequestDTO;

public class LoanEligibilityUtil {

    //To calculate maximum sanction loan
    public static double calculateMaxLoan(LoanRequestDTO dto) {
        double monthlyIncome = getMonthlyIncome(dto);
        double multiplier = getMultiplier(dto);
        return monthlyIncome * multiplier;
    }

    //Businessman ITR / 12 months
    public static double getMonthlyIncome(LoanRequestDTO dto) {
        if ((dto.getJobType().equalsIgnoreCase("Businessman") || dto.getJobType().equalsIgnoreCase("Housewife"))
                && dto.getAnnualItr() > 0) {
            return dto.getAnnualItr() / 12.0;
        }
        return dto.getMonthlyIncome();
    }

    //Monthly income EMI not exceed 50%
    public static boolean foirCheck(double requestedAmount, int tenureMonths, double monthlyIncome) {
        double simpleEmi = requestedAmount / tenureMonths;
        double maxEmi = monthlyIncome * 0.5;
        return simpleEmi <= maxEmi;
    }

    //Applicant age not exceed more than 50
    public static boolean tenureAgeCheck(int currentAge, int tenureMonths) {
        int ageAtEnd = currentAge + (tenureMonths / 12);
        return ageAtEnd <= 50;
    }

    //Assingn Multiplier to different domain
    private static double getMultiplier(LoanRequestDTO dto) {
        int age = dto.getAge();
        int creditScore = dto.getCreditScore();
        String jobType = dto.getJobType();
        boolean stableJob = dto.isStableJob();
        double annualItr = dto.getAnnualItr();

        if (jobType.equalsIgnoreCase("Salaried")) {
            if (age >= 21 && age <= 30) {
                if (creditScore > 750 && stableJob) return 20;
                if (creditScore >= 700 && stableJob) return 18;
                if (creditScore >= 650) return 15;
                return 10;
            }
            if (age >= 31 && age <= 40) {
                if (creditScore > 750 && stableJob) return 18;
                if (creditScore >= 700 && stableJob) return 16;
                if (creditScore >= 650) return 13;
                return 10;
            }
            if (age >= 41 && age <= 50) {
                if (creditScore > 750 && stableJob) return 15;
                if (creditScore >= 700 && stableJob) return 13;
                if (creditScore >= 650) return 12;
                return 10;
            }
        }

        if (jobType.equalsIgnoreCase("Businessman")) {
            if (creditScore > 750 && annualItr > 0) return 18;
            if (creditScore > 750) return 12;
            if (creditScore < 650 && annualItr > 0) return 12;
            return 8;
        }

        if (jobType.equalsIgnoreCase("Housewife")) {
            return 8;
        }

        return 10;
    }
}
