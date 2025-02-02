package com.hasandag.banking.loanapi.controller;

import com.hasandag.banking.loanapi.dto.LoanDTO;
import com.hasandag.banking.loanapi.dto.LoanInstallmentDTO;
import com.hasandag.banking.loanapi.entity.Loan;
import com.hasandag.banking.loanapi.entity.LoanInstallment;
import com.hasandag.banking.loanapi.payment.PaymentResult;
import com.hasandag.banking.loanapi.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/loans")
@RequiredArgsConstructor
public class LoanController {
    private final LoanService loanService;

    @PostMapping("/create")
    public Loan createLoan(@RequestBody LoanDTO request) {
        return loanService.createLoan(
                request.getCustomerId(),
                request.getAmount(),
                request.getInterestRate(),
                request.getNumberOfInstallments()
        );
    }

    @GetMapping("/list")
    public List<Loan> listLoans(@RequestParam Long customerId) {
        return loanService.findLoansByCustomer(customerId);
    }

    @GetMapping("/installments")
    public ResponseEntity<List<LoanInstallment>> listInstallments(@RequestParam Long loanId) {
        List<LoanInstallment> installments = loanService.findInstallmentsByLoanId(loanId);
        return ResponseEntity.ok(installments);
    }

    @PostMapping("/pay")
    public PaymentResult payLoan(@RequestBody LoanInstallmentDTO request) {
        return loanService.payLoanInstallments(request.getLoanId(), request.getAmount());
    }
}
