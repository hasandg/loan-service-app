package com.hasandag.banking.loanapi.mappers;


import com.hasandag.banking.loanapi.dto.LoanInstallmentDTO;
import com.hasandag.banking.loanapi.dto.LoanResponseDTO;
import com.hasandag.banking.loanapi.entity.Loan;
import com.hasandag.banking.loanapi.entity.LoanInstallment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface LoanMapper {

    LoanMapper INSTANCE = Mappers.getMapper(LoanMapper.class);

    LoanResponseDTO toLoanResponseDTO(Loan loan);

    List<LoanResponseDTO> toLoanResponseDTOs(List<Loan> loans);

    @Mapping(target = "loanId", source = "loan.id")
    LoanInstallmentDTO toLoanInstallmentDTO(LoanInstallment loanInstallment);

    List<LoanInstallmentDTO> toLoanInstallmentDTOs(List<LoanInstallment> installments);
}
