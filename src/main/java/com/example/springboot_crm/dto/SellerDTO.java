package com.example.springboot_crm.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class SellerDTO {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String contactInfo;

    private LocalDateTime registrationDate;


    public SellerDTO() {
    }

    public SellerDTO(Long id, String name, String contactInfo, LocalDateTime registrationDate) {
        this.id = id;
        this.name = name;
        this.contactInfo = contactInfo;
        this.registrationDate = registrationDate;
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
}
