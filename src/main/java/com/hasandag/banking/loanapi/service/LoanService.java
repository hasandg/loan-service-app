package com.hasandag.banking.loanapi.service;


import com.hasandag.banking.loanapi.entity.Customer;
import com.hasandag.banking.loanapi.entity.Loan;
import com.hasandag.banking.loanapi.entity.LoanInstallment;
import com.hasandag.banking.loanapi.payment.PaymentResult;
import com.hasandag.banking.loanapi.repository.CustomerRepository;
import com.hasandag.banking.loanapi.repository.LoanInstallmentRepository;
import com.hasandag.banking.loanapi.repository.LoanRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanService {
    private final CustomerRepository customerRepository;
    private final LoanRepository loanRepository;
    private final LoanInstallmentRepository installmentRepository;

    @Transactional
    public Loan createLoan(Long customerId, BigDecimal amount, BigDecimal interestRate, int installments) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));

        validateLoanConditions(customer, amount, interestRate, installments);

        Loan loan = new Loan();
        loan.setCustomer(customer);
        loan.setLoanAmount(amount);
        loan.setCreateDate(LocalDate.now());
        loan.setIsPaid(false);

        createInstallments(loan, amount, interestRate, installments);

        return loanRepository.save(loan);
    }

    public List<Loan> findLoansByCustomer(Long customerId) {
        return loanRepository.findByCustomerId(customerId);
    }

    public List<LoanInstallment> findInstallmentsByLoanId(Long loanId) {
        return null;
    }

    @Transactional
    public PaymentResult payLoanInstallments(Long loanId, BigDecimal paymentAmount) {
        List<LoanInstallment> unpaidInstallments = installmentRepository.findByLoanIdAndIsPaidFalse(loanId);

        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("Loan not found"));
        BigDecimal totalPaid = BigDecimal.ZERO;
        int installmentsPaid = 0;
        LocalDate currentDate = LocalDate.now();

        for (LoanInstallment installment : unpaidInstallments) {
            if (canPayInstallment(installment, currentDate) &&
                    totalPaid.add(installment.getAmount()).compareTo(paymentAmount) <= 0) {

                BigDecimal adjustedAmount = calculateAdjustedAmount(installment, currentDate);

                installment.setPaidAmount(adjustedAmount);
                installment.setPaymentDate(currentDate);
                installment.setIsPaid(true);

                totalPaid = totalPaid.add(adjustedAmount);
                installmentsPaid++;
            }
        }

        //update and save loan fields
        loan.setIsPaid(installmentsPaid == unpaidInstallments.size());
        loanRepository.save(loan);
        // update and save installments
        installmentRepository.saveAll(unpaidInstallments);

        return new PaymentResult(installmentsPaid, totalPaid, loan.getIsPaid());
    }

    private void createInstallments(Loan loan, BigDecimal amount, BigDecimal interestRate, int installments) {
        BigDecimal totalAmount = amount.multiply(interestRate.add(BigDecimal.ONE));
        BigDecimal installmentAmount = totalAmount.divide(BigDecimal.valueOf(installments), 2, RoundingMode.HALF_UP);

        for (int i = 1; i <= installments; i++) {
            LoanInstallment installment = new LoanInstallment();
            installment.setLoan(loan);
            installment.setAmount(installmentAmount);
            installment.setDueDate(LocalDate.now().plusMonths(i).withDayOfMonth(1));
            installment.setIsPaid(false);
            installmentRepository.save(installment);
        }
    }


    private BigDecimal calculateAdjustedAmount(LoanInstallment installment, LocalDate paymentDate) {
        long daysDifference = ChronoUnit.DAYS.between(installment.getDueDate(), paymentDate);

        if (daysDifference < 0) {
            return installment.getAmount().multiply(BigDecimal.valueOf(1 - 0.001 * Math.abs(daysDifference)));
        } else if (daysDifference > 0) {
            return installment.getAmount().multiply(BigDecimal.valueOf(1 + 0.001 * daysDifference));
        }

        return installment.getAmount();
    }

    private void validateLoanConditions(Customer customer, BigDecimal amount, BigDecimal interestRate, int installments) {
        if (interestRate.compareTo(BigDecimal.valueOf(0.1)) < 0 || interestRate.compareTo(BigDecimal.valueOf(0.5)) > 0) {
            throw new IllegalArgumentException("Interest rate must be between 0.1 and 0.5");
        }
        if (installments != 6 && installments != 9 && installments != 12 && installments != 24) {
            throw new IllegalArgumentException("Number of installments must be 6, 9, 12, or 24");
        }
        BigDecimal totalLoanAmount = amount.multiply(interestRate.add(BigDecimal.ONE));
        if (customer.getUsedCreditLimit().add(totalLoanAmount).compareTo(customer.getCreditLimit()) > 0) {
            throw new IllegalArgumentException("Customer does not have enough credit limit");
        }
    }

    private boolean canPayInstallment(LoanInstallment installment, LocalDate currentDate) {
        return !installment.getDueDate().isAfter(currentDate.plusMonths(3));
    }

}
