package com.example.springboot_crm.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "seller")
public class Seller {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", length = 30, nullable = false)
    private String name;

    @Column(name = "contact_info", length = 30, nullable = false, unique = true)
    private String contactInfo;

    @Column(name = "registration_date", nullable = false, updatable = false, columnDefinition = "timestamp default now()")
    private LocalDateTime registrationDate;

    public Seller() {
        this.registrationDate = LocalDateTime.now();
    }

    public Seller(String name, String contactInfo) {
        this.name = name;
        this.contactInfo = contactInfo;
        this.registrationDate = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }
}
