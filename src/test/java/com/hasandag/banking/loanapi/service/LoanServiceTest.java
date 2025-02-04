package com.hasandag.banking.loanapi.service;

import com.hasandag.banking.loanapi.dto.LoanResponseDTO;
import com.hasandag.banking.loanapi.dto.PaymentResultDTO;
import com.hasandag.banking.loanapi.entity.Customer;
import com.hasandag.banking.loanapi.entity.Loan;
import com.hasandag.banking.loanapi.entity.LoanInstallment;
import com.hasandag.banking.loanapi.enums.NumberOfInstallments;
import com.hasandag.banking.loanapi.repository.CustomerRepository;
import com.hasandag.banking.loanapi.repository.LoanInstallmentRepository;
import com.hasandag.banking.loanapi.repository.LoanRepository;
import com.hasandag.banking.loanapi.responsemodel.LoanApiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class LoanServiceTest {
    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private LoanInstallmentRepository installmentRepository;

    @InjectMocks
    private LoanService loanService;

    private Customer testCustomer;
    private Loan testLoan;

    @BeforeEach
    void setUp() {
        testCustomer = new Customer();
        testCustomer.setId(1L);
        testCustomer.setCreditLimit(BigDecimal.valueOf(20000));
        testCustomer.setUsedCreditLimit(BigDecimal.ZERO);

        testLoan = new Loan();
        testLoan.setId(1L);
        testLoan.setCustomer(testCustomer);
        testLoan.setLoanAmount(BigDecimal.valueOf(5000));
    }

    @Test
    void createLoan_ValidInput_ShouldReturnLoan() {
        when(customerRepository.findById(1L)).thenReturn(Optional.ofNullable(testCustomer));
        when(loanRepository.save(any(Loan.class))).thenReturn(testLoan);
        when(installmentRepository.saveAll(any())).thenReturn(List.of(new LoanInstallment()));

        ResponseEntity<LoanApiResponse<LoanResponseDTO>> createdLoan = loanService.createLoan(
                1L,
                BigDecimal.valueOf(5000),
                BigDecimal.valueOf(0.3),
                NumberOfInstallments.TWELVE
        );

        assertNotNull(createdLoan);
        verify(loanRepository).save(any(Loan.class));
        verify(installmentRepository, times(1)).saveAll(any());
    }

    @Test
    void createLoan_InvalidInput_ShouldReturnBadRequest() {
        when(customerRepository.findById(1L)).thenReturn(Optional.ofNullable(testCustomer));
        when(loanRepository.save(any(Loan.class))).thenReturn(testLoan);
        when(installmentRepository.saveAll(any())).thenReturn(List.of(new LoanInstallment()));

        ResponseEntity<LoanApiResponse<LoanResponseDTO>> createdLoan = loanService.createLoan(
                1L,
                BigDecimal.valueOf(50000),
                BigDecimal.valueOf(0.3),
                NumberOfInstallments.TWELVE
        );

        assertNull(Objects.requireNonNull(createdLoan.getBody()).getData());
        assertEquals(HttpStatus.BAD_REQUEST, createdLoan.getStatusCode());
    }

    @Test
    void payLoanInstallments_PartialPayment_ShouldProcessCorrectly() {

        LoanInstallment mockInstallment = createMockInstallment(1L, BigDecimal.valueOf(500));

        when(loanRepository.findById(testLoan.getId()))
                .thenReturn(Optional.ofNullable(testLoan));

        when(installmentRepository.findByLoanIdAndIsPaidFalse(testLoan.getId()))
                .thenReturn(List.of(mockInstallment));

        ResponseEntity<LoanApiResponse<PaymentResultDTO>> result = loanService.payLoanInstallments(
                1L,
                BigDecimal.valueOf(500)
        );

        assertEquals(1, Objects.requireNonNull(result.getBody()).getData().getInstallmentsPaid());
        verify(installmentRepository).findByLoanIdAndIsPaidFalse(testLoan.getId());
    }

    @Test
    void payLoanInstallments_MissingLoan_ShouldReturnEmptyBody() {

        LoanInstallment mockInstallment = createMockInstallment(1L, BigDecimal.valueOf(500));

        when(loanRepository.findById(testLoan.getId()))
                .thenReturn(Optional.empty());

        when(installmentRepository.findByLoanIdAndIsPaidFalse(testLoan.getId()))
                .thenReturn(List.of(mockInstallment));

        ResponseEntity<LoanApiResponse<PaymentResultDTO>> result = loanService.payLoanInstallments(
                1L,
                BigDecimal.valueOf(500)
        );

        assertNull(Objects.requireNonNull(result.getBody()).getData());
    }

    @Test
    void createLoan_ExceedCreditLimit_ShouldReturnFaultyResult() {
        testCustomer.setCreditLimit(BigDecimal.valueOf(4000));
        when(customerRepository.findById(2L)).thenReturn(Optional.ofNullable(testCustomer));

        assertEquals(HttpStatus.BAD_REQUEST, loanService.createLoan(
                2L,
                BigDecimal.valueOf(50000),
                BigDecimal.valueOf(0.3),
                NumberOfInstallments.TWELVE).getStatusCode());
    }

    @Test
    void payLoanInstallments_OverpaymentScenario_ShouldPayMultipleInstallments() {

        LoanInstallment installment1 = createMockInstallment(1L, BigDecimal.valueOf(500));
        LoanInstallment installment2 = createMockInstallment(2L, BigDecimal.valueOf(500));

        when(loanRepository.findById(testLoan.getId()))
                .thenReturn(Optional.ofNullable(testLoan));

        when(installmentRepository.findByLoanIdAndIsPaidFalse(testLoan.getId()))
                .thenReturn(List.of(installment1, installment2));

        ResponseEntity<LoanApiResponse<PaymentResultDTO>> result = loanService.payLoanInstallments(
                1L,
                BigDecimal.valueOf(1000)
        );

        assertEquals(2, Objects.requireNonNull(result.getBody()).getData().getInstallmentsPaid());
    }

    private LoanInstallment createMockInstallment(Long id, BigDecimal amount) {
        LoanInstallment installment = new LoanInstallment();
        installment.setId(id);
        installment.setLoan(testLoan);
        installment.setAmount(amount);
        installment.setDueDate(LocalDate.now().plusMonths(1));
        installment.setIsPaid(false);
        return installment;
    }
}