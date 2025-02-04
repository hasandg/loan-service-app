package com.hasandag.banking.loanapi.repository;

import com.hasandag.banking.loanapi.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByCustomerId(Long customerId);
    List<Loan> findByCustomerIdAndIsPaid(Long customerId, Boolean isPaid);}
