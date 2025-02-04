package com.hasandag.banking.loanapi.repository;

import com.hasandag.banking.loanapi.entity.LoanInstallment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanInstallmentRepository extends JpaRepository<LoanInstallment, Long> {
    List<LoanInstallment> findByLoanId(Long loanId);
    List<LoanInstallment> findByLoanIdAndIsPaidFalse(Long loanId);
}
