package com.hasandag.banking.loanapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hasandag.banking.loanapi.dto.LoanInstallmentDTO;
import com.hasandag.banking.loanapi.dto.LoanInstallmentPayRequestDTO;
import com.hasandag.banking.loanapi.dto.LoanRequestDTO;
import com.hasandag.banking.loanapi.dto.LoanResponseDTO;
import com.hasandag.banking.loanapi.dto.PaymentResultDTO;
import com.hasandag.banking.loanapi.enums.NumberOfInstallments;
import com.hasandag.banking.loanapi.responsemodel.LoanApiResponse;
import com.hasandag.banking.loanapi.service.LoanService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class LoanControllerTest {

    @Mock
    private LoanService loanService;

    @InjectMocks
    private LoanController loanController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(loanController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void createLoan_ShouldReturnCreatedLoan() throws Exception {
        LoanRequestDTO requestDTO = LoanRequestDTO.builder()
                .customerId(1L)
                .loanAmount(BigDecimal.valueOf(5000))
                .interestRate(BigDecimal.valueOf(0.3))
                .numberOfInstallments(NumberOfInstallments.TWELVE)
                .build();

        LoanResponseDTO responseDTO = LoanResponseDTO.builder()
                .id(1L)
                .build();

        LoanApiResponse<LoanResponseDTO> apiResponse = new LoanApiResponse<>(responseDTO, null);

        when(loanService.createLoan(any(), any(), any(), any())).thenReturn(new ResponseEntity<>(apiResponse, HttpStatus.CREATED));

        mockMvc.perform(post("/loans/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.id").value(1L));
    }

    @Test
    void listLoans_ShouldReturnListOfLoans() throws Exception {
        LoanResponseDTO responseDTO = LoanResponseDTO.builder()
                .id(1L)
                .build();

        LoanApiResponse<List<LoanResponseDTO>> apiResponse = new LoanApiResponse<>(Collections.singletonList(responseDTO), null);

        when(loanService.findLoansByCustomer(any(), any())).thenReturn(new ResponseEntity<>(apiResponse, HttpStatus.OK));

        mockMvc.perform(get("/loans/list")
                        .param("customerId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(1L));
    }

    @Test
    void listInstallments_ShouldReturnListOfInstallments() throws Exception {
        LoanInstallmentDTO installmentDTO = LoanInstallmentDTO.builder()
                .id(1L)
                .build();

        LoanApiResponse<List<LoanInstallmentDTO>> apiResponse = new LoanApiResponse<>(Collections.singletonList(installmentDTO), null);

        when(loanService.findInstallmentsByLoanId(any())).thenReturn(new ResponseEntity<>(apiResponse, HttpStatus.OK));

        mockMvc.perform(get("/loans/installments")
                        .param("loanId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(1L));
    }

    @Test
    void payLoan_ShouldReturnPaymentResult() throws Exception {
        LoanInstallmentPayRequestDTO requestDTO = LoanInstallmentPayRequestDTO.builder()
                .loanId(1L)
                .amount(BigDecimal.valueOf(500))
                .build();

        PaymentResultDTO paymentResultDTO = new PaymentResultDTO(1, BigDecimal.valueOf(500), true);

        LoanApiResponse<PaymentResultDTO> apiResponse = new LoanApiResponse<>(paymentResultDTO, null);

        when(loanService.payLoanInstallments(any(), any())).thenReturn(new ResponseEntity<>(apiResponse, HttpStatus.OK));

        mockMvc.perform(post("/loans/pay")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.installmentsPaid").value(1))
                .andExpect(jsonPath("$.data.totalPaid").value(500));
    }
}