package com.hasandag.banking.loanapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class PaymentResultDTO {
    private int installmentsPaid;
    private BigDecimal totalPaid;
    private boolean isLoanPaid;

}
