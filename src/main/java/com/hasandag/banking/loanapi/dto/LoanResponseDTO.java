package com.hasandag.banking.loanapi.dto;

import com.hasandag.banking.loanapi.enums.NumberOfInstallments;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanResponseDTO {
    private Long id;
    private BigDecimal loanAmount;
    private BigDecimal interestRate;
    private NumberOfInstallments numberOfInstallments;
    private LocalDate createDate;
    private Boolean isPaid;
}
