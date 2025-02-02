package com.hasandag.banking.loanapi.repository;

import com.hasandag.banking.loanapi.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findByName(String name);
}
