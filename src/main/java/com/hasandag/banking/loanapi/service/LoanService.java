package com.hasandag.banking.loanapi.service;

import com.hasandag.banking.loanapi.dto.LoanInstallmentDTO;
import com.hasandag.banking.loanapi.dto.LoanResponseDTO;
import com.hasandag.banking.loanapi.dto.PaymentResultDTO;
import com.hasandag.banking.loanapi.entity.Customer;
import com.hasandag.banking.loanapi.entity.Loan;
import com.hasandag.banking.loanapi.entity.LoanInstallment;
import com.hasandag.banking.loanapi.enums.NumberOfInstallments;
import com.hasandag.banking.loanapi.mappers.LoanMapper;
import com.hasandag.banking.loanapi.repository.CustomerRepository;
import com.hasandag.banking.loanapi.repository.LoanInstallmentRepository;
import com.hasandag.banking.loanapi.repository.LoanRepository;
import com.hasandag.banking.loanapi.responsemodel.LoanApiResponse;
import com.hasandag.banking.loanapi.responsemodel.LoanApiResponseMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class LoanService {
    private final CustomerRepository customerRepository;
    private final LoanRepository loanRepository;
    private final LoanInstallmentRepository installmentRepository;

    @Transactional
    public ResponseEntity<LoanApiResponse<LoanResponseDTO>> createLoan(Long customerId, BigDecimal amount, BigDecimal interestRate, NumberOfInstallments numberOfInstallments) {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (Objects.isNull(customer)) {
            return createLoanErrorResponse("Customer not found", HttpStatus.NOT_FOUND);
        }

        String validationError = validateLoanConditions(customer, amount, interestRate);
        if (Objects.nonNull(validationError)) {
            return createErrorResponse("Validation failed", validationError, HttpStatus.BAD_REQUEST);
        }

        Loan savedLoan = saveLoan(customer, amount, interestRate, numberOfInstallments);
        customer.setCreditLimit(customer.getCreditLimit().subtract(amount));
        customerRepository.save(customer);

        return new ResponseEntity<>(new LoanApiResponse<>(LoanMapper.INSTANCE.toLoanResponseDTO(savedLoan), HttpStatus.CREATED), HttpStatus.CREATED);
    }

    public ResponseEntity<LoanApiResponse<List<LoanResponseDTO>>> findLoansByCustomer(Long customerId, Boolean isPaid) {
        List<Loan> loans = (isPaid == null) ? loanRepository.findByCustomerId(customerId) : loanRepository.findByCustomerIdAndIsPaid(customerId, isPaid);
        return new ResponseEntity<>(new LoanApiResponse<>(LoanMapper.INSTANCE.toLoanResponseDTOs(loans), HttpStatus.OK), HttpStatus.OK);
    }

    public ResponseEntity<LoanApiResponse<List<LoanInstallmentDTO>>> findInstallmentsByLoanId(Long loanId) {
        List<LoanInstallment> loanInstallments = installmentRepository.findByLoanId(loanId);
        return new ResponseEntity<>(new LoanApiResponse<>(LoanMapper.INSTANCE.toLoanInstallmentDTOs(loanInstallments), HttpStatus.OK), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<LoanApiResponse<PaymentResultDTO>> payLoanInstallments(Long loanId, BigDecimal paymentAmount) {
        Loan loan = loanRepository.findById(loanId).orElse(null);

        if (Objects.isNull(loan)) {
            return createPaymentResultErrorResponse("Loan not found", HttpStatus.NOT_FOUND);
        }

        List<LoanInstallment> unpaidInstallments = installmentRepository.findByLoanIdAndIsPaidFalse(loanId);
        PaymentResultDTO paymentResult = processInstallments(unpaidInstallments, paymentAmount);

        loan.setIsPaid(paymentResult.getInstallmentsPaid() == unpaidInstallments.size());
        loanRepository.save(loan);
        installmentRepository.saveAll(unpaidInstallments);

        return new ResponseEntity<>(new LoanApiResponse<>(paymentResult, HttpStatus.OK), HttpStatus.OK);
    }

    private ResponseEntity<LoanApiResponse<LoanResponseDTO>> createLoanErrorResponse(String message, HttpStatus status) {
        LoanApiResponseMessage responseMessage = new LoanApiResponseMessage(message);
        LoanApiResponse<LoanResponseDTO> response = new LoanApiResponse<>(responseMessage, status);
        return new ResponseEntity<>(response, status);
    }

    private ResponseEntity<LoanApiResponse<PaymentResultDTO>> createPaymentResultErrorResponse(String message, HttpStatus status) {
        LoanApiResponseMessage responseMessage = new LoanApiResponseMessage(message);
        LoanApiResponse<PaymentResultDTO> response = new LoanApiResponse<>(responseMessage, status);
        return new ResponseEntity<>(response, status);
    }

    private ResponseEntity<LoanApiResponse<LoanResponseDTO>> createErrorResponse(String title, String details, HttpStatus status) {
        LoanApiResponseMessage responseMessage = new LoanApiResponseMessage(title, details);
        LoanApiResponse<LoanResponseDTO> response = new LoanApiResponse<>(responseMessage, status);
        return new ResponseEntity<>(response, status);
    }

    private Loan saveLoan(Customer customer, BigDecimal amount, BigDecimal interestRate, NumberOfInstallments numberOfInstallments) {
        Loan loan = Loan.builder()
                .customer(customer)
                .loanAmount(amount)
                .interestRate(interestRate)
                .numberOfInstallments(numberOfInstallments.getValue())
                .createDate(LocalDate.now())
                .isPaid(false)
                .build();

        Loan savedLoan = loanRepository.save(loan);
        createInstallments(savedLoan, amount, interestRate, numberOfInstallments.getValue());
        return savedLoan;
    }

    @Transactional
    private void createInstallments(Loan loan, BigDecimal amount, BigDecimal interestRate, int installments) {
        BigDecimal totalAmount = amount.multiply(interestRate.add(BigDecimal.ONE));
        BigDecimal installmentAmount = totalAmount.divide(BigDecimal.valueOf(installments), 2, RoundingMode.HALF_UP);

        List<LoanInstallment> installmentsList = IntStream.rangeClosed(1, installments)
                .mapToObj(i -> {
                    LoanInstallment installment = new LoanInstallment();
                    installment.setLoan(loan);
                    installment.setAmount(installmentAmount);
                    installment.setDueDate(LocalDate.now().plusMonths(i).withDayOfMonth(1));
                    installment.setIsPaid(false);
                    return installment;
                })
                .collect(Collectors.toList());

        installmentRepository.saveAll(installmentsList);
    }

    private PaymentResultDTO processInstallments(List<LoanInstallment> unpaidInstallments, BigDecimal paymentAmount) {
        BigDecimal totalPaid = BigDecimal.ZERO;
        int installmentsPaid = 0;
        LocalDate currentDate = LocalDate.now();

        for (LoanInstallment installment : unpaidInstallments) {
            if (isPaymentDueInThreeMonths(installment, currentDate)) {
                BigDecimal adjustedAmount = calculateAdjustedAmount(installment, currentDate);
                if (adjustedAmount.compareTo(paymentAmount) <= 0) {
                    totalPaid = processInstallment(installment, adjustedAmount, currentDate, totalPaid);
                    paymentAmount = paymentAmount.subtract(adjustedAmount);
                    installmentsPaid++;
                } else {
                    break;
                }
            }
        }

        return new PaymentResultDTO(installmentsPaid, totalPaid, installmentsPaid == unpaidInstallments.size());
    }

    private BigDecimal processInstallment(LoanInstallment installment, BigDecimal adjustedAmount, LocalDate currentDate, BigDecimal totalPaid) {
        installment.setPaidAmount(adjustedAmount);
        installment.setPaymentDate(currentDate);
        installment.setIsPaid(true);
        return totalPaid.add(adjustedAmount);
    }

    private BigDecimal calculateAdjustedAmount(LoanInstallment installment, LocalDate paymentDate) {
        long daysDifference = ChronoUnit.DAYS.between(installment.getDueDate(), paymentDate);
        return installment.getAmount().multiply(BigDecimal.valueOf(1 + 0.001 * daysDifference));
    }

    private String validateLoanConditions(Customer customer, BigDecimal amount, BigDecimal interestRate) {
        BigDecimal totalLoanAmount = amount.multiply(interestRate.add(BigDecimal.ONE));
        if (customer.getUsedCreditLimit().add(totalLoanAmount).compareTo(customer.getCreditLimit()) > 0) {
            return "Customer does not have enough credit limit";
        }
        return null;
    }

    private boolean isPaymentDueInThreeMonths(LoanInstallment installment, LocalDate currentDate) {
        return !installment.getDueDate().isAfter(currentDate.plusMonths(3));
    }
}