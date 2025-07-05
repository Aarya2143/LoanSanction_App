package com.loan_sanction.service;

import com.loan_sanction.entities.LoanApplication;
import com.loan_sanction.exception.LoanProcessingException;
import com.loan_sanction.repository.LoanApplicationRepository;
import com.loan_sanction.service.impl.LoanEligibilityUtil;
import com.loan_sanction.service.impl.LoanServiceImpl;

import com.loan_sanction.util.TestConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import static com.loan_sanction.util.TestConstants.TEST_DTO_VALID;
import static com.loan_sanction.util.TestConstants.TEST_DTO_AMOUNT_TOO_LOW;
import static com.loan_sanction.util.TestConstants.TEST_DTO_AMOUNT_TOO_HIGH;
import static com.loan_sanction.util.TestConstants.TEST_DTO_EXCEEDS_ELIGIBLE;
import static com.loan_sanction.util.TestConstants.TEST_DTO_FOIR_FAIL;
import static com.loan_sanction.util.TestConstants.TEST_DTO_TENURE_AGE_FAIL;

class LoanServiceImplTest {

    @Mock
    private LoanApplicationRepository repo;

    @InjectMocks
    private LoanServiceImpl service;

    @BeforeEach
    void setUp() {
        repo = mock(LoanApplicationRepository.class);
        service = new LoanServiceImpl(repo);
    }

    @Test
    void testCalculateMaxLoan_delegatesToUtil() {
        double maxLoan = service.calculateMaxLoan(TEST_DTO_VALID);
        assertEquals(LoanEligibilityUtil.calculateMaxLoan(TEST_DTO_VALID), maxLoan, 0.01);
    }

    @Test
    void testProcessLoan_success() {
        when(repo.save(any(LoanApplication.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        LoanApplication loan = service.processLoan(TEST_DTO_VALID);

        assertNotNull(loan);
        assertEquals("Alice", loan.getApplicantName());
        assertEquals(200000, loan.getSanctionedAmount());
    }

    @Test
    void testProcessLoan_amountTooLow() {
        LoanProcessingException ex = assertThrows(LoanProcessingException.class,
                () -> service.processLoan(TEST_DTO_AMOUNT_TOO_LOW));
        assertTrue(ex.getMessage().contains("Loan amount must be between"));
    }

    @Test
    void testProcessLoan_amountTooHigh() {
        LoanProcessingException ex = assertThrows(LoanProcessingException.class,
                () -> service.processLoan(TEST_DTO_AMOUNT_TOO_HIGH));
        assertTrue(ex.getMessage().contains("Loan amount must be between"));
    }

    @Test
    void testProcessLoan_exceedsMaxEligible() {
        LoanProcessingException ex = assertThrows(LoanProcessingException.class,
                () -> service.processLoan(TEST_DTO_EXCEEDS_ELIGIBLE));
        assertTrue(ex.getMessage().contains("Exceeds your maximum eligible loan"));
    }

    @Test
    void testProcessLoan_foirFails() {
        try (MockedStatic<LoanEligibilityUtil> mockedStatic = mockStatic(LoanEligibilityUtil.class)) {
            mockedStatic.when(() -> LoanEligibilityUtil.calculateMaxLoan(TEST_DTO_FOIR_FAIL)).thenReturn(600000.0);
            mockedStatic.when(() -> LoanEligibilityUtil.getMonthlyIncome(TEST_DTO_FOIR_FAIL)).thenReturn(20000.0);
            mockedStatic.when(() -> LoanEligibilityUtil.foirCheck(500000, 12, 20000)).thenReturn(false);
            mockedStatic.when(() -> LoanEligibilityUtil.tenureAgeCheck(30, 12)).thenReturn(true);

            LoanProcessingException ex = assertThrows(LoanProcessingException.class,
                    () -> service.processLoan(TEST_DTO_FOIR_FAIL));

            assertTrue(ex.getMessage().toLowerCase().contains("emi exceeds 50%"),
                    "Expected error message to mention EMI exceeds 50%");
        }
    }

    @Test
    void testProcessLoan_tenureAgeCheckFails() {
        LoanProcessingException ex = assertThrows(LoanProcessingException.class,
                () -> service.processLoan(TEST_DTO_TENURE_AGE_FAIL));
        assertTrue(ex.getMessage().contains("Loan tenure exceeds age 50 limit"));
    }

    @Test
    void testGetLoanDetails_success() {
        LoanApplication app = TestConstants.getSampleLoanApplication();

        when(repo.findById(TestConstants.LOAN_ID)).thenReturn(Optional.of(app));

        LoanApplication result = service.getLoanDetails(TestConstants.LOAN_ID);

        assertNotNull(result);
        assertEquals(TestConstants.APPLICANT_NAME, result.getApplicantName());
        assertEquals(TestConstants.SANCTIONED_AMOUNT, result.getSanctionedAmount());
    }

    @Test
    void testGetLoanDetails_notFound() {
        when(repo.findById(TestConstants.INVALID_LOAN_ID)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.getLoanDetails(TestConstants.INVALID_LOAN_ID));

        assertTrue(ex.getMessage().contains("Loan application not found"));
    }
}