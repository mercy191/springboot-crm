package com.example.springboot_crm.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class SellerWithTotalAmountDTO {

    private Long id;

    private String name;

    private String contactInfo;

    private LocalDateTime registrationDate;

    private BigDecimal totalTransactionAmount;

    public SellerWithTotalAmountDTO() {
    }

    public SellerWithTotalAmountDTO(SellerDTO sellerDTO, BigDecimal totalTransactionAmount) {
        this.id = sellerDTO.getId();
        this.name = sellerDTO.getName();
        this.contactInfo = sellerDTO.getContactInfo();
        this.registrationDate = sellerDTO.getRegistrationDate();
        this.totalTransactionAmount = totalTransactionAmount;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public BigDecimal getTotalTransactionAmount() {
        return totalTransactionAmount;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    public void setTotalTransactionAmount(BigDecimal totalTransactionAmount) {
        this.totalTransactionAmount = totalTransactionAmount;
    }
}
