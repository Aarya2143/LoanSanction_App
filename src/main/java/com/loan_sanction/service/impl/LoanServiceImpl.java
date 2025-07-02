package com.loan_sanction.service.impl;

import com.loan_sanction.dto.LoanRequestDTO;
import com.loan_sanction.entities.LoanApplication;
import com.loan_sanction.repository.LoanApplicationRepository;
import com.loan_sanction.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoanServiceImpl implements LoanService {

    private final LoanApplicationRepository repo;

    @Autowired
    public LoanServiceImpl(LoanApplicationRepository repo) {
        this.repo = repo;
    }

    @Override
    public double calculateMaxLoan(LoanRequestDTO dto) {
        return LoanEligibilityUtil.calculateMaxLoan(dto);
    }

    @Override
    public LoanApplication processLoan(LoanRequestDTO dto) {
        double maxLoan = calculateMaxLoan(dto);

        if (dto.getRequestedAmount() < 50000 || dto.getRequestedAmount() > 4000000) {
            throw new RuntimeException("Loan amount must be between ₹50,000 and ₹40,00,000");
        }

        if (dto.getRequestedAmount() > maxLoan) {
            throw new RuntimeException("Exceeds your maximum eligible loan: ₹" + maxLoan);
        }

        double monthlyIncome = LoanEligibilityUtil.getMonthlyIncome(dto);

        if (!LoanEligibilityUtil.foirCheck(dto.getRequestedAmount(), dto.getTenureMonths(), monthlyIncome)) {
            throw new RuntimeException("EMI exceeds 50% of monthly income.");
        }

        if (!LoanEligibilityUtil.tenureAgeCheck(dto.getAge(), dto.getTenureMonths())) {
            throw new RuntimeException("Loan tenure exceeds age 50 limit.");
        }

        LoanApplication loan = new LoanApplication();
        loan.setApplicantName(dto.getApplicantName());
        loan.setAge(dto.getAge());
        loan.setJobType(dto.getJobType());
        loan.setMonthlyIncome(dto.getMonthlyIncome());
        loan.setCreditScore(dto.getCreditScore());
        loan.setRequestedAmount(dto.getRequestedAmount());
        loan.setTenureMonths(dto.getTenureMonths());
        loan.setSanctionedAmount(dto.getRequestedAmount());

        return repo.save(loan);
    }

    @Override
    public LoanApplication getLoanDetails(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Loan application not found for ID: " + id));
    }


}