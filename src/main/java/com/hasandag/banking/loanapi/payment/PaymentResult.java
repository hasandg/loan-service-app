package com.hasandag.banking.loanapi.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class PaymentResult {
    private int installmentsPaid;
    private BigDecimal totalPaid;
    private boolean isLoanPaid;

}
