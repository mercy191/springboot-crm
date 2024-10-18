package com.example.springboot_crm.mapper;

import com.example.springboot_crm.dto.SellerDTO;
import com.example.springboot_crm.entity.Seller;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class SellerMapperTests {

    private final SellerMapper sellerMapper = new SellerMapper();

    @Test
    @DisplayName("Test toDTO - Success")
    public void testToDTO_Success() {

        Seller seller = new Seller();
        seller.setId(1L);
        seller.setName("John Doe");
        seller.setContactInfo("john@example.com");
        seller.setRegistrationDate(LocalDateTime.now());

        SellerDTO dto = sellerMapper.toDTO(seller);

        assertAll(
                () -> assertNotNull(dto),
                () -> assertEquals(seller.getId(), dto.getId()),
                () -> assertEquals(seller.getName(), dto.getName()),
                () -> assertEquals(seller.getContactInfo(), dto.getContactInfo()),
                () -> assertEquals(seller.getRegistrationDate(), dto.getRegistrationDate())
        );
    }

    @Test
    @DisplayName("Test toDTO - Null seller")
    public void testToDTO_NullSeller() {

        SellerDTO dto = sellerMapper.toDTO(null);

        assertNull(dto);
    }


    @Test
    @DisplayName("Test toEntity - Success")
    public void testToEntity_Success() {

        SellerDTO dto = new SellerDTO();
        dto.setName("John Doe");
        dto.setContactInfo("john@example.com");


        Seller seller = sellerMapper.toEntity(dto);

        assertAll(
                () -> assertNotNull(seller),
                () -> assertNull(seller.getId()),
                () -> assertEquals(dto.getName(), seller.getName()),
                () -> assertEquals(dto.getContactInfo(), seller.getContactInfo()),
                () -> assertNotNull(seller.getRegistrationDate())
        );
    }

    @Test
    @DisplayName("Test toEntity - Null DTO")
    public void testToEntity_NullDTO() {

        Seller seller = sellerMapper.toEntity(null);

        assertNull(seller);
    }
}
