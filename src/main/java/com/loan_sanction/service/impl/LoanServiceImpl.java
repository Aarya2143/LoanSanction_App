package com.loan_sanction.service.impl;

import com.loan_sanction.dto.LoanRequestDTO;
import com.loan_sanction.entities.LoanApplication;
import com.loan_sanction.exception.LoanProcessingException;
import com.loan_sanction.repository.LoanApplicationRepository;
import com.loan_sanction.service.LoanService;
import com.loan_sanction.util.LoanConstants;
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

        if (dto.getRequestedAmount() < LoanConstants.MIN_LOAN_AMOUNT || dto.getRequestedAmount() > LoanConstants.MAX_LOAN_AMOUNT) {
            throw new LoanProcessingException(LoanConstants.MSG_LOAN_AMOUNT_LIMIT);
        }

        if (dto.getRequestedAmount() > maxLoan) {
            throw new LoanProcessingException(String.format(LoanConstants.MSG_EXCEEDS_MAX_ELIGIBILITY, maxLoan));
        }

        double monthlyIncome = LoanEligibilityUtil.getMonthlyIncome(dto);

        if (!LoanEligibilityUtil.foirCheck(dto.getRequestedAmount(), dto.getTenureMonths(), monthlyIncome)) {
            throw new LoanProcessingException(LoanConstants.MSG_EMI_EXCEEDS_LIMIT);
        }

        if (!LoanEligibilityUtil.tenureAgeCheck(dto.getAge(), dto.getTenureMonths())) {
            throw new LoanProcessingException(LoanConstants.MSG_TENURE_AGE_LIMIT);
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
                .orElseThrow(() -> new RuntimeException(LoanConstants.MSG_LOAN_NOT_FOUND + id));
    }
}