package com.example.springboot_crm.controller;

import com.example.springboot_crm.dto.TransactionDTO;
import com.example.springboot_crm.entity.PaymentType;
import com.example.springboot_crm.exception.ResourceNotFoundException;
import com.example.springboot_crm.service.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
public class TransactionControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @DisplayName("Test getAllTransactions - Valid input returns 200")
    public void test_whenValidInput_thenGetAllTransactions_andReturns200() throws Exception {

        TransactionDTO transaction1 = new TransactionDTO(1L, 1L, BigDecimal.valueOf(1000), PaymentType.CARD, LocalDateTime.now());
        TransactionDTO transaction2 = new TransactionDTO(2L, 1L, BigDecimal.valueOf(2000), PaymentType.TRANSFER, LocalDateTime.now());

        when(transactionService.getAllTransactions()).thenReturn(List.of(transaction1, transaction2));

        mockMvc.perform(get("/api/springboot_crm/transactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].sellerId").value(1))
                .andExpect(jsonPath("$[0].amount").value(1000))
                .andExpect(jsonPath("$[0].paymentType").value("CARD"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].sellerId").value(1))
                .andExpect(jsonPath("$[1].amount").value(2000))
                .andExpect(jsonPath("$[1].paymentType").value("TRANSFER"));

        verify(transactionService, times(1)).getAllTransactions();
    }

    @Test
    @DisplayName("Test getAllTransactions - Valid input returns 200")
    public void test_whenValidInput_thenGetAllTransactions_andReturns404() throws Exception {

        when(transactionService.getAllTransactions())
                .thenThrow(new ResourceNotFoundException("No transactions found"));

        mockMvc.perform(get("/api/springboot_crm/transactions"))
                .andExpect(status().isNotFound());
    }



    @Test
    @DisplayName("Test getTransactionById - Valid path variable returns 200")
    public void test_whenValidPathVariable_thenGetTransactionById_andReturns200() throws Exception {

        TransactionDTO transaction = new TransactionDTO(1L, 1L, BigDecimal.valueOf(1000), PaymentType.CARD, LocalDateTime.now());

        when(transactionService.getTransactionById(1L)).thenReturn(transaction);

        mockMvc.perform(get("/api/springboot_crm/transactions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.sellerId").value(1))
                .andExpect(jsonPath("$.amount").value(1000))
                .andExpect(jsonPath("$.paymentType").value("CARD"));

        verify(transactionService, times(1)).getTransactionById(1L);
    }

    @Test
    @DisplayName("Test getTransactionById - Not valid path variable returns 404")
    public void test_whenNotValidPathVariable_thenGetTransactionById_andReturns404() throws Exception {

        when(transactionService.getTransactionById(1L))
                .thenThrow(new ResourceNotFoundException("Transaction not found with id " + 1));

        mockMvc.perform(get("/api/springboot_crm/transactions/1"))
                .andExpect(status().isNotFound());
    }



    @Test
    @DisplayName("Test getTransactionsBySellerId - Valid path variable returns 200")
    public void test_whenValidPathVariable_thenGetTransactionsBySellerId_andReturns200() throws Exception {

        TransactionDTO transaction1 = new TransactionDTO(1L, 1L, BigDecimal.valueOf(1000), PaymentType.CARD, LocalDateTime.now());
        TransactionDTO transaction2 = new TransactionDTO(2L, 1L, BigDecimal.valueOf(2000), PaymentType.TRANSFER, LocalDateTime.now());

        when(transactionService.getTransactionsBySellerId(1L)).thenReturn(List.of(transaction1, transaction2));

        mockMvc.perform(get("/api/springboot_crm/transactions/sellerId/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].sellerId").value(1))
                .andExpect(jsonPath("$[0].amount").value(1000))
                .andExpect(jsonPath("$[0].paymentType").value("CARD"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].sellerId").value(1))
                .andExpect(jsonPath("$[1].amount").value(2000))
                .andExpect(jsonPath("$[1].paymentType").value("TRANSFER"));

        verify(transactionService, times(1)).getTransactionsBySellerId(1L);
    }

    @Test
    @DisplayName("Test getTransactionsBySellerId - Not valid path variable returns 404")
    public void test_whenNotValidPathVariable_thenGetTransactionsBySellerId_andReturns404() throws Exception {

        when(transactionService.getTransactionsBySellerId(1L))
                .thenThrow(new ResourceNotFoundException("No transactions found for seller with id " + 1));

        mockMvc.perform(get("/api/springboot_crm/transactions/sellerId/1"))
                .andExpect(status().isNotFound());
    }



    @Test
    @DisplayName("Test createTransaction - Valid input returns 201")
    public void test_whenValidInput_thenCreateTransaction_andReturns201() throws Exception {

        Long sellerId = 1L;
        TransactionDTO transaction = new TransactionDTO(null, 1L, BigDecimal.valueOf(1000), PaymentType.CARD, null);
        TransactionDTO createdTransaction = new TransactionDTO(1L,  1L, BigDecimal.valueOf(1000), PaymentType.CARD, LocalDateTime.now());

        when(transactionService.createTransaction(Mockito.eq(sellerId), Mockito.any(TransactionDTO.class))).thenReturn(createdTransaction);

        mockMvc.perform(post("/api/springboot_crm/transactions")
                        .param("sellerId", String.valueOf(sellerId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transaction)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.sellerId").value(1))
                .andExpect(jsonPath("$.amount").value(1000))
                .andExpect(jsonPath("$.paymentType").value("CARD"));

        verify(transactionService, times(1)).createTransaction(Mockito.eq(sellerId), Mockito.any(TransactionDTO.class));
    }

    @Test
    @DisplayName("Test createTransaction - Valid input returns 404")
    public void test_whenValidInput_thenCreateTransaction_andReturns404() throws Exception {

        TransactionDTO transaction = new TransactionDTO(null, 1L, BigDecimal.valueOf(1000), PaymentType.CARD, null);

        when(transactionService.createTransaction(1L, transaction))
                .thenThrow(new ResourceNotFoundException("Seller not found with id 1"));

        mockMvc.perform(post("/api/springboot_crm/transactions")
                        .param("sellerId", String.valueOf(1L))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transaction)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Test createTransaction - Missing parameter returns 400")
    public void test_whenMissingRequestParameter_thenNotCreateTransaction_andReturns400() throws Exception {

        TransactionDTO transaction = new TransactionDTO(null, null, BigDecimal.valueOf(1000), PaymentType.CARD, null);

        mockMvc.perform(post("/api/springboot_crm/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transaction)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test createTransaction - Invalid input returns 400")
    public void test_whenNotValidRequestBody_thenNotCreateTransaction_andReturns400() throws Exception {

        TransactionDTO invalidTransaction = new TransactionDTO(null, null, BigDecimal.valueOf(1000), PaymentType.CARD,  null);

        mockMvc.perform(post("/api/springboot_crm/transactions")
                        .param("sellerId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidTransaction)))
                .andExpect(status().isBadRequest());
    }

}
