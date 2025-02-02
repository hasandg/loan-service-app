package com.hasandag.banking.loanapi.mappers;


import com.hasandag.banking.loanapi.dto.CustomerDTO;
import com.hasandag.banking.loanapi.dto.LoanDTO;
import com.hasandag.banking.loanapi.dto.LoanInstallmentDTO;
import com.hasandag.banking.loanapi.entity.Customer;
import com.hasandag.banking.loanapi.entity.Loan;
import com.hasandag.banking.loanapi.entity.LoanInstallment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LoanMapper {
    LoanMapper INSTANCE = Mappers.getMapper(LoanMapper.class);

    CustomerDTO customerToDTO(Customer customer);
    Customer dtoToCustomer(CustomerDTO dto);

    @Mapping(source = "customer.id", target = "customerId")
    LoanDTO loanToDTO(Loan loan);

    @Mapping(source = "customerId", target = "customer.id")
    Loan dtoToLoan(LoanDTO dto);

    @Mapping(source = "loan.id", target = "loanId")
    LoanInstallmentDTO installmentToDTO(LoanInstallment installment);

    @Mapping(source = "loanId", target = "loan.id")
    LoanInstallment dtoToInstallment(LoanInstallmentDTO dto);
}
