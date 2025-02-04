package com.hasandag.banking.loanapi.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class LoanInstallmentPayRequestDTO {
    private Long loanId;
    private BigDecimal amount;
}
