package com.hasandag.banking.loanapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
public class Loan extends BasePojo{

    @ManyToOne(fetch = FetchType.LAZY)
    private Customer customer;

    private BigDecimal loanAmount;

    private BigDecimal interestRate;

    private Integer numberOfInstallments;

    private LocalDate createDate;

    private Boolean isPaid = false;

}
