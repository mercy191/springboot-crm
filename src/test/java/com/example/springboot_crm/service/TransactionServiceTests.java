package com.example.springboot_crm.service;

import com.example.springboot_crm.dto.TransactionDTO;
import com.example.springboot_crm.entity.PaymentType;
import com.example.springboot_crm.entity.Seller;
import com.example.springboot_crm.entity.Transaction;
import com.example.springboot_crm.exception.ResourceNotFoundException;
import com.example.springboot_crm.mapper.TransactionMapper;
import com.example.springboot_crm.repository.ISellerRepository;
import com.example.springboot_crm.repository.ITransactionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TransactionServiceTests {

    @Autowired
    private TransactionService transactionService;

    @MockBean
    private ITransactionRepository transactionRepository;

    @MockBean
    private ISellerRepository sellerRepository;

    @MockBean
    private TransactionMapper transactionMapper;


    @Test
    @DisplayName("Test getAllTransactions - Success")
    public void testGetAllTransactions_Success() {

        Transaction transaction1 = new Transaction();
        transaction1.setId(1L);
        transaction1.setAmount(new BigDecimal("100.00"));
        transaction1.setPaymentType(PaymentType.CASH);
        transaction1.setTransactionDate(LocalDateTime.now());

        Transaction transaction2 = new Transaction();
        transaction2.setId(2L);
        transaction2.setAmount(new BigDecimal("200.00"));
        transaction2.setPaymentType(PaymentType.CARD);
        transaction2.setTransactionDate(LocalDateTime.now());

        List<Transaction> transactions = Arrays.asList(transaction1, transaction2);

        TransactionDTO transactionDTO1 = new TransactionDTO();
        transactionDTO1.setId(transaction1.getId());
        transactionDTO1.setAmount(transaction1.getAmount());
        transactionDTO1.setPaymentType(transaction1.getPaymentType());
        transactionDTO1.setTransactionDate(transaction1.getTransactionDate());

        TransactionDTO transactionDTO2 = new TransactionDTO();
        transactionDTO2.setId(transaction2.getId());
        transactionDTO2.setAmount(transaction2.getAmount());
        transactionDTO2.setPaymentType(transaction2.getPaymentType());
        transactionDTO2.setTransactionDate(transaction2.getTransactionDate());

        when(transactionRepository.findAll()).thenReturn(transactions);
        when(transactionMapper.toDTO(transaction1)).thenReturn(transactionDTO1);
        when(transactionMapper.toDTO(transaction2)).thenReturn(transactionDTO2);

        List<TransactionDTO> result = transactionService.getAllTransactions();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(transactionDTO1));
        assertTrue(result.contains(transactionDTO2));

        verify(transactionRepository, times(1)).findAll();
        verify(transactionMapper, times(1)).toDTO(transaction1);
        verify(transactionMapper, times(1)).toDTO(transaction2);
    }

    @Test
    @DisplayName("Test getAllTransactions - Transactions not found")
    public void testGetAllTransactions_NoTransactionsFound() {

        when(transactionRepository.findAll()).thenReturn(Collections.emptyList());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            transactionService.getAllTransactions();
        });

        assertEquals("No transactions found", exception.getMessage());

        verify(transactionRepository, times(1)).findAll();
        verify(transactionMapper, never()).toDTO(any(Transaction.class));
    }


    @Test
    @DisplayName("Test getTransactionById - Success")
    public void testGetTransactionById_Success() {

        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setAmount(new BigDecimal("100.00"));
        transaction.setPaymentType(PaymentType.CASH);
        transaction.setTransactionDate(LocalDateTime.now());

        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setId(transaction.getId());
        transactionDTO.setAmount(transaction.getAmount());
        transactionDTO.setPaymentType(transaction.getPaymentType());
        transactionDTO.setTransactionDate(transaction.getTransactionDate());

        when(transactionRepository.findById(transaction.getId())).thenReturn(Optional.of(transaction));
        when(transactionMapper.toDTO(transaction)).thenReturn(transactionDTO);

        TransactionDTO result = transactionService.getTransactionById(transaction.getId());

        assertNotNull(result);
        assertEquals(transaction.getId(), result.getId());
        assertEquals(transaction.getAmount(), result.getAmount());
        assertEquals(transaction.getPaymentType(), result.getPaymentType());
        assertEquals(transaction.getTransactionDate(), result.getTransactionDate());

        verify(transactionRepository, times(1)).findById(transaction.getId());
        verify(transactionMapper, times(1)).toDTO(transaction);
    }

    @Test
    @DisplayName("Test getTransactionById - Not found")
    public void testGetTransactionById_NotFound() {
        Long transactionId = 1L;

        when(transactionRepository.findById(transactionId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            transactionService.getTransactionById(transactionId);
        });

        assertEquals("Transaction not found with id 1", exception.getMessage());

        verify(transactionRepository, times(1)).findById(transactionId);
        verify(transactionMapper, never()).toDTO(any(Transaction.class));
    }


    @Test
    @DisplayName("Test getTransactionsBySellerId - Success")
    public void testGetTransactionsBySellerId_Success() {

        Seller seller = new Seller();
        seller.setId(1L);
        seller.setName("John Doe");
        seller.setContactInfo("john@example.com");
        seller.setRegistrationDate(LocalDateTime.now());

        Transaction transaction1 = new Transaction();
        transaction1.setId(1L);
        transaction1.setSeller(seller);
        transaction1.setAmount(new BigDecimal("100.00"));
        transaction1.setPaymentType(PaymentType.CASH);
        transaction1.setTransactionDate(LocalDateTime.now());

        Transaction transaction2 = new Transaction();
        transaction2.setId(2L);
        transaction2.setSeller(seller);
        transaction2.setAmount(new BigDecimal("200.00"));
        transaction2.setPaymentType(PaymentType.CARD);
        transaction2.setTransactionDate(LocalDateTime.now());

        List<Transaction> transactions = Arrays.asList(transaction1, transaction2);

        TransactionDTO transactionDTO1 = new TransactionDTO();
        transactionDTO1.setId(transaction1.getId());
        transactionDTO1.setAmount(transaction1.getAmount());
        transactionDTO1.setPaymentType(transaction1.getPaymentType());
        transactionDTO1.setTransactionDate(transaction1.getTransactionDate());

        TransactionDTO transactionDTO2 = new TransactionDTO();
        transactionDTO2.setId(transaction2.getId());
        transactionDTO2.setAmount(transaction2.getAmount());
        transactionDTO2.setPaymentType(transaction2.getPaymentType());
        transactionDTO2.setTransactionDate(transaction2.getTransactionDate());

        when(transactionRepository.findBySellerId(seller.getId())).thenReturn(transactions);
        when(transactionMapper.toDTO(transaction1)).thenReturn(transactionDTO1);
        when(transactionMapper.toDTO(transaction2)).thenReturn(transactionDTO2);

        List<TransactionDTO> result = transactionService.getTransactionsBySellerId(seller.getId());

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(transactionDTO1));
        assertTrue(result.contains(transactionDTO2));

        verify(transactionRepository, times(1)).findBySellerId(seller.getId());
        verify(transactionMapper, times(1)).toDTO(transaction1);
        verify(transactionMapper, times(1)).toDTO(transaction2);
    }

    @Test
    @DisplayName("Test getTransactionsBySellerId - Transactions not found")
    public void testGetTransactionsBySellerId_NoTransactionsFound() {

        Long sellerId = 1L;

        when(transactionRepository.findBySellerId(sellerId)).thenReturn(Collections.emptyList());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            transactionService.getTransactionsBySellerId(sellerId);
        });

        assertEquals("No transactions found for seller with id 1", exception.getMessage());

        verify(transactionRepository, times(1)).findBySellerId(sellerId);
        verify(transactionMapper, never()).toDTO(any(Transaction.class));
    }


    @Test
    @DisplayName("Test createTransaction - Success")
    public void testCreateTransaction_Success() {

        Seller seller = new Seller();
        seller.setId(1L);
        seller.setName("John Doe");
        seller.setContactInfo("john.doe@example.com");
        seller.setRegistrationDate(LocalDateTime.now());

        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setAmount(new BigDecimal("150.00"));
        transactionDTO.setPaymentType(PaymentType.TRANSFER);
        transactionDTO.setTransactionDate(LocalDateTime.now());
        transactionDTO.setSellerId(seller.getId());

        Transaction transactionEntity = new Transaction();
        transactionEntity.setAmount(transactionDTO.getAmount());
        transactionEntity.setPaymentType(transactionDTO.getPaymentType());
        transactionEntity.setTransactionDate(transactionDTO.getTransactionDate());
        transactionEntity.setSeller(seller);

        Transaction savedTransaction = new Transaction();
        savedTransaction.setId(1L);
        savedTransaction.setAmount(transactionEntity.getAmount());
        savedTransaction.setPaymentType(transactionEntity.getPaymentType());
        savedTransaction.setTransactionDate(transactionEntity.getTransactionDate());
        savedTransaction.setSeller(transactionEntity.getSeller());

        TransactionDTO savedTransactionDTO = new TransactionDTO();
        savedTransactionDTO.setId(savedTransaction.getId());
        savedTransactionDTO.setAmount(savedTransaction.getAmount());
        savedTransactionDTO.setPaymentType(savedTransaction.getPaymentType());
        savedTransactionDTO.setTransactionDate(savedTransaction.getTransactionDate());
        savedTransactionDTO.setSellerId(savedTransaction.getId());

        when(sellerRepository.findById(seller.getId())).thenReturn(Optional.of(seller));
        when(transactionMapper.toEntity(transactionDTO)).thenReturn(transactionEntity);
        when(transactionRepository.save(transactionEntity)).thenReturn(savedTransaction);
        when(transactionMapper.toDTO(savedTransaction)).thenReturn(savedTransactionDTO);

        TransactionDTO result = transactionService.createTransaction(seller.getId(), transactionDTO);

        assertNotNull(result);
        assertEquals(savedTransactionDTO.getId(), result.getId());
        assertEquals(savedTransactionDTO.getAmount(), result.getAmount());
        assertEquals(savedTransactionDTO.getPaymentType(), result.getPaymentType());
        assertEquals(savedTransaction.getTransactionDate(), result.getTransactionDate());

        verify(sellerRepository, times(1)).findById(seller.getId());
        verify(transactionMapper, times(1)).toEntity(transactionDTO);
        verify(transactionRepository, times(1)).save(transactionEntity);
        verify(transactionMapper, times(1)).toDTO(savedTransaction);
    }

    @Test
    @DisplayName("Test createTransaction - Seller not found")
    public void testCreateTransaction_SellerNotFound() {

        Long sellerId = 1L;
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setAmount(new BigDecimal("150.00"));
        transactionDTO.setPaymentType(PaymentType.TRANSFER);
        transactionDTO.setTransactionDate(LocalDateTime.now());

        when(sellerRepository.findById(sellerId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            transactionService.createTransaction(sellerId, transactionDTO);
        });

        assertEquals("Seller not found with id 1", exception.getMessage());

        verify(sellerRepository, times(1)).findById(sellerId);
        verify(transactionMapper, never()).toEntity(any(TransactionDTO.class));
        verify(transactionRepository, never()).save(any(Transaction.class));
        verify(transactionMapper, never()).toDTO(any(Transaction.class));
    }
}

