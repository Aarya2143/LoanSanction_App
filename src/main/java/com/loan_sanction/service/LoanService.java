package com.loan_sanction.service;

import com.loan_sanction.dto.LoanRequestDTO;
import com.loan_sanction.entities.LoanApplication;

public interface LoanService {

    double calculateMaxLoan(LoanRequestDTO dto);
    LoanApplication processLoan(LoanRequestDTO dto);
    LoanApplication getLoanDetails(Long id);

}