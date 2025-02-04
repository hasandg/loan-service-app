package com.hasandag.banking.loanapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
public class LoanInstallment extends BasePojo{

    @ManyToOne(fetch = FetchType.LAZY)
    private Loan loan;

    private BigDecimal amount;

    private BigDecimal paidAmount = BigDecimal.ZERO;

    private LocalDate dueDate;

    private LocalDate paymentDate;

    private Boolean isPaid = false;

}
