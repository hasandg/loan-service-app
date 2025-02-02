package com.hasandag.banking.loanapi.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
public class LoanDTO {
    private Long id;
    private Long customerId;
    private BigDecimal amount;
    private BigDecimal interestRate;
    private Integer numberOfInstallments;
    private LocalDate createDate;
    private Boolean isPaid;
}
