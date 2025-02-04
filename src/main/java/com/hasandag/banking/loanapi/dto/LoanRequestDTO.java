package com.hasandag.banking.loanapi.dto;

import com.hasandag.banking.loanapi.enums.NumberOfInstallments;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
public class LoanRequestDTO {

    private final String ZERO_POINT_ONE = "0.1";
    private final String ZERO_POINT_FIVE = "0.5";

    private Long id;
    private Long customerId;
    private BigDecimal loanAmount;

    @DecimalMin(value = ZERO_POINT_ONE, message = "Interest rate must be at least " + ZERO_POINT_ONE)
    @DecimalMax(value = ZERO_POINT_FIVE, message = "Interest rate must be at most " + ZERO_POINT_FIVE)
    private BigDecimal interestRate;

    private NumberOfInstallments numberOfInstallments;
    private LocalDate createDate;
    private Boolean isPaid;
}
