package com.example.springboot_crm.mapper;

import com.example.springboot_crm.dto.SellerDTO;
import com.example.springboot_crm.entity.Seller;
import org.springframework.stereotype.Component;

@Component
public class SellerMapper {

    public SellerDTO toDTO(Seller seller) {
        if (seller == null) {
            return null;
        }

        SellerDTO dto = new SellerDTO();
        dto.setId(seller.getId());
        dto.setName(seller.getName());
        dto.setContactInfo(seller.getContactInfo());
        dto.setRegistrationDate(seller.getRegistrationDate());
        // totalTransactionAmount будет установлен из репозитория
        return dto;
    }

    public Seller toEntity(SellerDTO dto) {
        if (dto == null) {
            return null;
        }

        Seller seller = new Seller();
        // id устанавливается автоматически.
        seller.setName(dto.getName());
        seller.setContactInfo(dto.getContactInfo());
        // registrationDate устанавливается автоматически.
        return seller;
    }
}
