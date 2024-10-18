package com.example.springboot_crm.mapper;

import com.example.springboot_crm.dto.TransactionDTO;
import com.example.springboot_crm.entity.PaymentType;
import com.example.springboot_crm.entity.Seller;
import com.example.springboot_crm.entity.Transaction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionMapperTests {

    private final TransactionMapper transactionMapper = new TransactionMapper();

    @Test
    @DisplayName("Test toDTO - Success")
    public void testToDTO_Success() {

        Seller seller = new Seller();
        seller.setId(1L);
        seller.setName("John Doe");
        seller.setContactInfo("john@example.com");
        seller.setRegistrationDate(LocalDateTime.now());

        Transaction transaction = new Transaction();
        transaction.setId(100L);
        transaction.setAmount(new BigDecimal("250.00"));
        transaction.setPaymentType(PaymentType.CARD);
        transaction.setTransactionDate(LocalDateTime.of(2023, 5, 20, 14, 30));
        transaction.setSeller(seller);

        TransactionDTO dto = transactionMapper.toDTO(transaction);

        assertAll(
                () -> assertNotNull(dto),
                () -> assertEquals(transaction.getId(), dto.getId()),
                () -> assertEquals(transaction.getAmount(), dto.getAmount()),
                () -> assertEquals(transaction.getPaymentType(), dto.getPaymentType()),
                () -> assertEquals(transaction.getTransactionDate(), dto.getTransactionDate()),
                () -> assertEquals(seller.getId(), dto.getSellerId())
        );


    }

    @Test
    @DisplayName("Test toDTO - Null transaction")
    public void testToDTO_NullTransaction() {

        TransactionDTO dto = transactionMapper.toDTO(null);

        assertNull(dto);
    }


    @Test
    @DisplayName("Test toEntity - Success")
    public void testToEntity_Success() {

        TransactionDTO dto = new TransactionDTO();
        dto.setAmount(new BigDecimal("250.00"));
        dto.setPaymentType(PaymentType.CARD);

        Transaction transaction = transactionMapper.toEntity(dto);

        assertAll(
                () -> assertNotNull(transaction),
                () -> assertNull(transaction.getId()),
                () -> assertEquals(transaction.getAmount(), dto.getAmount()),
                () -> assertEquals(transaction.getPaymentType(), dto.getPaymentType()),
                () -> assertNotNull(transaction.getTransactionDate()),
                () -> assertNull(transaction.getSeller())
        );
    }

    @Test
    @DisplayName("Test toEntity - Null DTO")
    public void testToEntity_NullDTO() {

        Transaction transaction = transactionMapper.toEntity(null);

        assertNull(transaction);
    }
}
