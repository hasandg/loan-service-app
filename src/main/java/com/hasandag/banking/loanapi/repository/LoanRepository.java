package com.hasandag.banking.loanapi.repository;

import com.hasandag.banking.loanapi.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByCustomerId(Long customerId);
    List<Loan> findByCustomerIdAndIsPaid(Long customerId, Boolean isPaid);}
