package com.example.springboot_crm.dto;

import com.example.springboot_crm.entity.PaymentType;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionDTO {

    private Long id;

    private Long sellerId;

    @NotNull
    private BigDecimal amount;

    @NotNull
    private PaymentType paymentType;

    private LocalDateTime transactionDate;

    // Конструкторы

    public TransactionDTO() {
    }

    public TransactionDTO(Long id, Long sellerId, BigDecimal amount, PaymentType paymentType, LocalDateTime transactionDate) {
        this.id = id;
        this.sellerId = sellerId;
        this.amount = amount;
        this.paymentType = paymentType;
        this.transactionDate = transactionDate;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }
}
