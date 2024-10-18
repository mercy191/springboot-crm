package com.example.springboot_crm.mapper;

import com.example.springboot_crm.dto.TransactionDTO;
import com.example.springboot_crm.entity.Transaction;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    public TransactionDTO toDTO(Transaction transaction) {
        if (transaction == null) {
            return null;
        }

        TransactionDTO dto = new TransactionDTO();
        dto.setId(transaction.getId());
        dto.setSellerId(transaction.getSeller().getId());
        dto.setAmount(transaction.getAmount());
        dto.setPaymentType(transaction.getPaymentType());
        dto.setTransactionDate(transaction.getTransactionDate());
        return dto;
    }

    public Transaction toEntity(TransactionDTO dto) {
        if (dto == null) {
            return null;
        }

        Transaction transaction = new Transaction();
        // id устанавливается автоматически.
        // Устанавливать продавца нужно через сервис, чтобы избежать ручного маппинга.
        transaction.setAmount(dto.getAmount());
        transaction.setPaymentType(dto.getPaymentType());
        // transactionDate устанавливается автоматически.
        return transaction;
    }
}
