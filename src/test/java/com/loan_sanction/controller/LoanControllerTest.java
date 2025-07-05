package com.loan_sanction.controller;

import com.loan_sanction.dto.LoanRequestDTO;
import com.loan_sanction.entities.LoanApplication;
import com.loan_sanction.service.LoanService;
import com.loan_sanction.util.LoanConstants;
import com.loan_sanction.util.TestConstants;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;

class LoanControllerTest {

    @Mock
    private LoanService service;

    @InjectMocks
    private LoanController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testApplyForLoan_success() {
        LoanRequestDTO dto = TestConstants.getSampleRequestDTO();
        LoanApplication loan = TestConstants.getSampleLoanApplication();

        when(service.processLoan(dto)).thenReturn(loan);

        ResponseEntity<Map<String, Object>> response = controller.applyForLoan(dto);

        assertEquals(LoanConstants.MSG_LOAN_SANCTIONED, response.getBody().get(LoanConstants.KEY_MESSAGE));
        assertEquals(TestConstants.SANCTIONED_AMOUNT, response.getBody().get(LoanConstants.KEY_SANCTIONED_AMOUNT));
    }

    @Test
    void testApplyForLoan_error() {
        LoanRequestDTO dto = TestConstants.getSampleRequestDTO();

        doThrow(new RuntimeException(TestConstants.ERROR_SERVICE)).when(service).processLoan(dto);

        Exception ex = assertThrows(RuntimeException.class, () -> controller.applyForLoan(dto));
        assertEquals(TestConstants.ERROR_SERVICE, ex.getMessage());
    }

    @Test
    void testCheckMaxEligibility_success() {
        LoanRequestDTO dto = TestConstants.getSampleRequestDTO();

        when(service.calculateMaxLoan(dto)).thenReturn(TestConstants.MAX_ELIGIBLE_AMOUNT);

        ResponseEntity<Map<String, Object>> response = controller.checkMaxEligibility(dto);

        assertEquals(LoanConstants.MSG_MAX_ELIGIBILITY, response.getBody().get(LoanConstants.KEY_MESSAGE));
        assertEquals(TestConstants.MAX_ELIGIBLE_AMOUNT, response.getBody().get(LoanConstants.KEY_MAX_ELIGIBLE_AMOUNT));
    }

    @Test
    void testCheckMaxEligibility_error() {
        LoanRequestDTO dto = TestConstants.getSampleRequestDTO();

        doThrow(new RuntimeException(TestConstants.ERROR_ELIGIBILITY)).when(service).calculateMaxLoan(dto);

        Exception ex = assertThrows(RuntimeException.class, () -> controller.checkMaxEligibility(dto));
        assertEquals(TestConstants.ERROR_ELIGIBILITY, ex.getMessage());
    }

    @Test
    void testGetLoanDetails_success() {
        LoanApplication loan = TestConstants.getSampleLoanApplication();

        when(service.getLoanDetails(TestConstants.LOAN_ID)).thenReturn(loan);

        ResponseEntity<LoanApplication> response = controller.getLoanDetails(TestConstants.LOAN_ID);

        assertEquals(TestConstants.APPLICANT_NAME, response.getBody().getApplicantName());
        assertEquals(TestConstants.SANCTIONED_AMOUNT, response.getBody().getSanctionedAmount());
    }

    @Test
    void testGetLoanDetails_notFound() {
        when(service.getLoanDetails(TestConstants.INVALID_LOAN_ID)).thenThrow(new RuntimeException(TestConstants.ERROR_NOT_FOUND));

        Exception ex = assertThrows(RuntimeException.class, () -> controller.getLoanDetails(TestConstants.INVALID_LOAN_ID));
        assertEquals(TestConstants.ERROR_NOT_FOUND, ex.getMessage());
    }
}
