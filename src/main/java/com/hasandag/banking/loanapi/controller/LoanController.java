package com.hasandag.banking.loanapi.controller;

import com.hasandag.banking.loanapi.dto.LoanInstallmentDTO;
import com.hasandag.banking.loanapi.dto.LoanInstallmentPayRequestDTO;
import com.hasandag.banking.loanapi.dto.LoanRequestDTO;
import com.hasandag.banking.loanapi.dto.LoanResponseDTO;
import com.hasandag.banking.loanapi.dto.PaymentResultDTO;
import com.hasandag.banking.loanapi.responsemodel.LoanApiResponse;
import com.hasandag.banking.loanapi.service.LoanService;
import jakarta.validation.constraints.NotNull;
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
    public ResponseEntity<LoanApiResponse<LoanResponseDTO>> createLoan(@RequestBody LoanRequestDTO request) {
        return loanService.createLoan(
                request.getCustomerId(),
                request.getLoanAmount(),
                request.getInterestRate(),
                request.getNumberOfInstallments()
        );
    }

    @GetMapping("/list")
    public ResponseEntity<LoanApiResponse<List<LoanResponseDTO>>> listLoans(@RequestParam @NotNull Long customerId, @RequestParam(required = false) Boolean isPaid) {
        return loanService.findLoansByCustomer(customerId, isPaid);
    }

    @GetMapping("/installments")
    public ResponseEntity<LoanApiResponse<List<LoanInstallmentDTO>>> listInstallments(@RequestParam @NotNull Long loanId) {
        return loanService.findInstallmentsByLoanId(loanId);
    }

    @PostMapping("/pay")
    public ResponseEntity<LoanApiResponse<PaymentResultDTO>> payLoan(@RequestBody LoanInstallmentPayRequestDTO request) {
        return loanService.payLoanInstallments(request.getLoanId(), request.getAmount());
    }
}
