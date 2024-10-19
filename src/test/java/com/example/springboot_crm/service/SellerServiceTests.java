package com.example.springboot_crm.service;

import com.example.springboot_crm.dto.SellerDTO;
import com.example.springboot_crm.dto.SellerWithTotalAmountDTO;
import com.example.springboot_crm.entity.PaymentType;
import com.example.springboot_crm.entity.Seller;
import com.example.springboot_crm.entity.Transaction;
import com.example.springboot_crm.exception.ResourceNotFoundException;
import com.example.springboot_crm.mapper.SellerMapper;
import com.example.springboot_crm.repository.ISellerRepository;
import com.example.springboot_crm.repository.ITransactionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class SellerServiceTests {

    @Autowired
    private SellerService sellerService;

    @MockBean
    private ISellerRepository iSellerRepository;

    @MockBean
    private ITransactionRepository iTransactionRepository;

    @MockBean
    private SellerMapper sellerMapper;


    @Test
    @DisplayName("Test getAllSellers - Success")
    public void testGetAllSellers_Success() {

        Seller seller1 = new Seller();
        seller1.setId(1L);
        seller1.setName("John Doe");
        seller1.setContactInfo("john@example.com");
        seller1.setRegistrationDate(LocalDateTime.now());

        Seller seller2 = new Seller();
        seller2.setId(2L);
        seller2.setName("Jane Smith");
        seller2.setContactInfo("jane@example.com");
        seller2.setRegistrationDate(LocalDateTime.now());

        SellerDTO sellerDTO1 = new SellerDTO();
        sellerDTO1.setId(seller1.getId());
        sellerDTO1.setName(seller1.getName());
        sellerDTO1.setContactInfo(seller1.getContactInfo());
        sellerDTO1.setRegistrationDate(seller1.getRegistrationDate());

        SellerDTO sellerDTO2 = new SellerDTO();
        sellerDTO2.setId(seller2.getId());
        sellerDTO2.setName(seller2.getName());
        sellerDTO2.setContactInfo(seller2.getContactInfo());
        sellerDTO2.setRegistrationDate(seller2.getRegistrationDate());

        List<Seller> sellers = Arrays.asList(seller1, seller2);

        when(iSellerRepository.findAll()).thenReturn(sellers);
        when(sellerMapper.toDTO(seller1)).thenReturn(sellerDTO1);
        when(sellerMapper.toDTO(seller2)).thenReturn(sellerDTO2);

        List<SellerDTO> result = sellerService.getAllSellers();

        assertNotNull(result);
        assertEquals(sellers.size(), result.size());
        assertEquals(sellers.get(0).getName(), result.get(0).getName());
        assertEquals(sellers.get(1).getName(), result.get(1).getName());

        verify(iSellerRepository, times(1)).findAll();
        verify(sellerMapper, times(1)).toDTO(seller1);
        verify(sellerMapper, times(1)).toDTO(seller2);
    }

    @Test
    @DisplayName("Test getAllSellers - Seller not found")
    public void testGetAllSellers_NoSellersFound() {

        when(iSellerRepository.findAll()).thenReturn(Collections.emptyList());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            sellerService.getAllSellers();
        });

        assertEquals("No sellers found", exception.getMessage());

        verify(iSellerRepository, times(1)).findAll();
        verify(sellerMapper, never()).toDTO(any());
    }



    @Test
    @DisplayName("Test getSellerById - Success")
    public void testGetSellerById_Success() {

        Seller seller = new Seller();
        seller.setId(1L);
        seller.setName("John Doe");
        seller.setContactInfo("john@example.com");
        seller.setRegistrationDate(LocalDateTime.now());

        SellerDTO sellerDTO = new SellerDTO();
        sellerDTO.setId(seller.getId());
        sellerDTO.setName(seller.getName());
        sellerDTO.setContactInfo(seller.getContactInfo());
        sellerDTO.setRegistrationDate(seller.getRegistrationDate());

        when(iSellerRepository.findById(seller.getId())).thenReturn(Optional.of(seller));
        when(sellerMapper.toDTO(seller)).thenReturn(sellerDTO);

        SellerDTO result = sellerService.getSellerById(seller.getId());

        // Assert
        assertNotNull(result);
        assertEquals(seller.getId(), result.getId());
        assertEquals(seller.getName(), result.getName());
        assertEquals(seller.getContactInfo(), result.getContactInfo());

        verify(iSellerRepository, times(1)).findById(seller.getId());
        verify(sellerMapper, times(1)).toDTO(seller);
    }

    @Test
    @DisplayName("Test getSellerById - Sellers not found")
    public void testGetSellerById_NotFound() {

        Long sellerId = 1L;
        when(iSellerRepository.findById(sellerId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            sellerService.getSellerById(sellerId);
        });

        assertEquals("Seller not found with id 1", exception.getMessage());

        verify(iSellerRepository, times(1)).findById(sellerId);
        verify(sellerMapper, never()).toDTO(any());
    }



    @Test
    @DisplayName("Test getMostProductiveSeller - Success")
    public void testGetMostProductiveSeller_Success() {

        LocalDateTime start = LocalDateTime.of(2023, 1, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2023, 12, 31, 23, 59);

        Seller seller1 = new Seller();
        seller1.setId(1L);
        seller1.setName("John Doe");
        seller1.setContactInfo("john@example.com");
        seller1.setRegistrationDate(LocalDateTime.now());

        Seller seller2 = new Seller();
        seller2.setId(2L);
        seller2.setName("Jane Smith");
        seller2.setContactInfo("jane@example.com");
        seller2.setRegistrationDate(LocalDateTime.now());

        Transaction transaction1 = new Transaction();
        transaction1.setId(1L);
        transaction1.setSeller(seller1);
        transaction1.setAmount(new BigDecimal("100.00"));
        transaction1.setPaymentType(PaymentType.CASH);
        transaction1.setTransactionDate(LocalDateTime.of(2023, 5, 15, 12, 0));

        Transaction transaction2 = new Transaction();
        transaction2.setId(2L);
        transaction2.setSeller(seller2);
        transaction2.setAmount(new BigDecimal("200.00"));
        transaction2.setPaymentType(PaymentType.CARD);
        transaction2.setTransactionDate(LocalDateTime.of(2023, 6, 20, 14, 0));

        List<Seller> sellers = Arrays.asList(seller1, seller2);
        List<Transaction> transactions1 = List.of(transaction1);
        List<Transaction> transactions2 = List.of(transaction2);

        when(iSellerRepository.findAll()).thenReturn(sellers);
        when(iTransactionRepository.findBySellerAndTransactionDateBetween(seller1, start, end)).thenReturn(transactions1);
        when(iTransactionRepository.findBySellerAndTransactionDateBetween(seller2, start, end)).thenReturn(transactions2);

        SellerDTO sellerDTO1 = new SellerDTO();
        sellerDTO1.setId(seller1.getId());
        sellerDTO1.setName(seller1.getName());
        sellerDTO1.setContactInfo(seller1.getContactInfo());
        sellerDTO1.setRegistrationDate(seller1.getRegistrationDate());

        SellerDTO sellerDTO2 = new SellerDTO();
        sellerDTO2.setId(seller2.getId());
        sellerDTO2.setName(seller2.getName());
        sellerDTO2.setContactInfo(seller2.getContactInfo());
        sellerDTO2.setRegistrationDate(seller2.getRegistrationDate());

        when(sellerMapper.toDTO(seller1)).thenReturn(sellerDTO1);
        when(sellerMapper.toDTO(seller2)).thenReturn(sellerDTO2);

        SellerWithTotalAmountDTO result = sellerService.getMostProductiveSeller(start, end);

        assertNotNull(result);
        assertEquals(seller2.getId(), result.getId());
        assertEquals("Jane Smith", result.getName());
        assertEquals(new BigDecimal("200.00"), result.getTotalTransactionAmount());

        verify(iSellerRepository, times(1)).findAll();
        verify(iTransactionRepository, times(1)).findBySellerAndTransactionDateBetween(seller1, start, end);
        verify(iTransactionRepository, times(1)).findBySellerAndTransactionDateBetween(seller2, start, end);
        verify(sellerMapper, times(1)).toDTO(any(Seller.class));
        verify(sellerMapper, times(1)).toDTO(any(Seller.class));
    }

    @Test
    @DisplayName("Test getMostProductiveSeller - Sellers not found")
    public void testGetMostProductiveSeller_NoSellersFound() {
        // Arrange
        LocalDateTime start = LocalDateTime.of(2023, 1, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2023, 12, 31, 23, 59);

        when(iSellerRepository.findAll()).thenReturn(Collections.emptyList());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            sellerService.getMostProductiveSeller(start, end);
        }, "Должно выброситься исключение ResourceNotFoundException");

        assertEquals("No most productive seller found", exception.getMessage(), "Сообщение исключения должно быть 'No most productive seller found'");

        verify(iSellerRepository, times(1)).findAll();
        verify(iTransactionRepository, never()).findBySellerAndTransactionDateBetween(any(), any(), any());
        verify(sellerMapper, never()).toDTO(any());
    }



    @Test
    @DisplayName("Test getSellersWithTotalAmountLessThan - Success")
    public void testGetSellersWithTotalAmountLessThan_Success() {

        BigDecimal amount = new BigDecimal("300.00");
        LocalDateTime start = LocalDateTime.of(2023, 1, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2023, 12, 31, 23, 59);

        Seller seller1 = new Seller();
        seller1.setId(1L);
        seller1.setName("John Doe");
        seller1.setContactInfo("john@example.com");
        seller1.setRegistrationDate(LocalDateTime.now());

        Seller seller2 = new Seller();
        seller2.setId(2L);
        seller2.setName("Jane Smith");
        seller2.setContactInfo("jane@example.com");
        seller2.setRegistrationDate(LocalDateTime.now());

        List<Seller> sellers = Arrays.asList(seller1, seller2);

        Transaction transaction1 = new Transaction();
        transaction1.setId(1L);
        transaction1.setSeller(seller1);
        transaction1.setAmount(new BigDecimal("100.00"));
        transaction1.setPaymentType(PaymentType.CASH);
        transaction1.setTransactionDate(LocalDateTime.of(2023, 5, 15, 12, 0));

        Transaction transaction2 = new Transaction();
        transaction2.setId(2L);
        transaction2.setSeller(seller1);
        transaction2.setAmount(new BigDecimal("150.00"));
        transaction2.setPaymentType(PaymentType.CARD);
        transaction2.setTransactionDate(LocalDateTime.of(2023, 6, 20, 14, 0));

        Transaction transaction3 = new Transaction();
        transaction3.setId(3L);
        transaction3.setSeller(seller2);
        transaction3.setAmount(new BigDecimal("200.00"));
        transaction3.setPaymentType(PaymentType.TRANSFER);
        transaction3.setTransactionDate(LocalDateTime.of(2023, 7, 25, 16, 0));

        List<Transaction> transactions1 = Arrays.asList(transaction1, transaction2); // Total: 250.00
        List<Transaction> transactions2 = Collections.singletonList(transaction3);   // Total: 200.00

        SellerDTO sellerDTO1 = new SellerDTO();
        sellerDTO1.setId(seller1.getId());
        sellerDTO1.setName(seller1.getName());
        sellerDTO1.setContactInfo(seller1.getContactInfo());
        sellerDTO1.setRegistrationDate(seller1.getRegistrationDate());

        SellerDTO sellerDTO2 = new SellerDTO();
        sellerDTO2.setId(seller2.getId());
        sellerDTO2.setName(seller2.getName());
        sellerDTO2.setContactInfo(seller2.getContactInfo());
        sellerDTO2.setRegistrationDate(seller2.getRegistrationDate());

        when(iSellerRepository.findAll()).thenReturn(sellers);
        when(iTransactionRepository.findBySellerAndTransactionDateBetween(seller1, start, end)).thenReturn(transactions1);
        when(iTransactionRepository.findBySellerAndTransactionDateBetween(seller2, start, end)).thenReturn(transactions2);
        when(sellerMapper.toDTO(seller1)).thenReturn(sellerDTO1);
        when(sellerMapper.toDTO(seller2)).thenReturn(sellerDTO2);

        List<SellerWithTotalAmountDTO> result = sellerService.getSellersWithTotalAmountLessThan(amount, start, end);

        assertNotNull(result);
        assertEquals(2, result.size());

        SellerWithTotalAmountDTO resultSeller1 = result.stream()
                .filter(s -> s.getId().equals(seller1.getId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Seller 1 not found"));

        SellerWithTotalAmountDTO resultSeller2 = result.stream()
                .filter(s -> s.getId().equals(seller2.getId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Seller 2 not found"));

        assertEquals(new BigDecimal("250.00"), resultSeller1.getTotalTransactionAmount());
        assertEquals(new BigDecimal("200.00"), resultSeller2.getTotalTransactionAmount());

        verify(iSellerRepository, times(1)).findAll();
        verify(iTransactionRepository, times(1)).findBySellerAndTransactionDateBetween(seller1, start, end);
        verify(iTransactionRepository, times(1)).findBySellerAndTransactionDateBetween(seller2, start, end);
        verify(sellerMapper, times(1)).toDTO(seller1);
        verify(sellerMapper, times(1)).toDTO(seller2);
    }

    @Test
    @DisplayName("Test getSellersWithTotalAmountLessThan - Sellers not found")
    public void testGetSellersWithTotalAmountLessThan_NoSellersFound() {

        BigDecimal amount = new BigDecimal("100.00");
        LocalDateTime start = LocalDateTime.of(2023, 1, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2024, 1, 1, 0, 0);

        Seller seller1 = new Seller();
        seller1.setId(1L);
        seller1.setName("John Doe");
        seller1.setContactInfo("john.doe@example.com");
        seller1.setRegistrationDate(LocalDateTime.now());

        List<Seller> sellers = Collections.singletonList(seller1);

        Transaction transaction1 = new Transaction();
        transaction1.setId(1L);
        transaction1.setSeller(seller1);
        transaction1.setAmount(new BigDecimal("150.00"));
        transaction1.setPaymentType(PaymentType.CASH);
        transaction1.setTransactionDate(LocalDateTime.of(2023, 6, 15, 0, 0));

        List<Transaction> transactions1 = Collections.singletonList(transaction1); // Total: 150.00

        SellerDTO sellerDTO1 = new SellerDTO();
        sellerDTO1.setId(seller1.getId());
        sellerDTO1.setName(seller1.getName());
        sellerDTO1.setContactInfo(seller1.getContactInfo());
        sellerDTO1.setRegistrationDate(seller1.getRegistrationDate());

        when(iSellerRepository.findAll()).thenReturn(sellers);
        when(iTransactionRepository.findBySellerAndTransactionDateBetween(seller1, start, end)).thenReturn(transactions1);
        when(sellerMapper.toDTO(seller1)).thenReturn(sellerDTO1);


        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            sellerService.getSellersWithTotalAmountLessThan(amount, start, end);
        });

        assertEquals("No sellers found with total transaction amount less than 100.00", exception.getMessage());

        verify(iSellerRepository, times(1)).findAll();
        verify(iTransactionRepository, times(1)).findBySellerAndTransactionDateBetween(seller1, start, end);
        verify(sellerMapper, never()).toDTO(seller1);
    }



    @Test
    @DisplayName("Test createSeller - Success")
    public void testCreateSeller_Success() {

        SellerDTO sellerDTO = new SellerDTO();
        sellerDTO.setName("New Seller");
        sellerDTO.setContactInfo("newseller@example.com");
        sellerDTO.setRegistrationDate(LocalDateTime.now());

        Seller sellerEntity = new Seller();
        sellerEntity.setId(1L);
        sellerEntity.setName(sellerDTO.getName());
        sellerEntity.setContactInfo(sellerDTO.getContactInfo());
        sellerEntity.setRegistrationDate(sellerDTO.getRegistrationDate());

        SellerDTO savedSellerDTO = new SellerDTO();
        savedSellerDTO.setId(sellerEntity.getId());
        savedSellerDTO.setName(sellerEntity.getName());
        savedSellerDTO.setContactInfo(sellerEntity.getContactInfo());
        savedSellerDTO.setRegistrationDate(sellerEntity.getRegistrationDate());

        when(sellerMapper.toEntity(sellerDTO)).thenReturn(sellerEntity);
        when(iSellerRepository.save(sellerEntity)).thenReturn(sellerEntity);
        when(sellerMapper.toDTO(sellerEntity)).thenReturn(savedSellerDTO);

        // Act
        SellerDTO result = sellerService.createSeller(sellerDTO);

        // Assert
        assertNotNull(result);
        assertEquals(sellerEntity.getId(), result.getId());
        assertEquals(sellerEntity.getName(), result.getName());
        assertEquals(sellerEntity.getContactInfo(), result.getContactInfo());

        verify(sellerMapper, times(1)).toEntity(sellerDTO);
        verify(iSellerRepository, times(1)).save(sellerEntity);
        verify(sellerMapper, times(1)).toDTO(sellerEntity);
    }



    @Test
    @DisplayName("Test updateSeller - Success")
    public void testUpdateSeller_Success() {

        Seller existingSeller = new Seller();
        existingSeller.setId(1L);
        existingSeller.setName("Old Name");
        existingSeller.setContactInfo("old@example.com");
        existingSeller.setRegistrationDate(LocalDateTime.now());

        SellerDTO updateDTO = new SellerDTO();
        updateDTO.setName("Updated Name");
        updateDTO.setContactInfo("updated@example.com");

        Seller updatedSeller = new Seller();
        updatedSeller.setId(existingSeller.getId());
        updatedSeller.setName(updateDTO.getName());
        updatedSeller.setContactInfo(updateDTO.getContactInfo());
        updatedSeller.setRegistrationDate(existingSeller.getRegistrationDate());

        SellerDTO updatedSellerDTO = new SellerDTO();
        updatedSellerDTO.setId(existingSeller.getId());
        updatedSellerDTO.setName(updatedSeller.getName());
        updatedSellerDTO.setContactInfo(updatedSeller.getContactInfo());
        updatedSellerDTO.setRegistrationDate(existingSeller.getRegistrationDate());

        when(iSellerRepository.findById(existingSeller.getId())).thenReturn(Optional.of(existingSeller));
        when(iSellerRepository.save(existingSeller)).thenReturn(updatedSeller);
        when(sellerMapper.toDTO(updatedSeller)).thenReturn(updatedSellerDTO);

        // Act
        SellerDTO result = sellerService.updateSeller(existingSeller.getId(), updateDTO);

        // Assert
        assertNotNull(result);
        assertEquals(existingSeller.getId(), result.getId());
        assertEquals(updatedSeller.getName(), result.getName());
        assertEquals(updatedSeller.getContactInfo(), result.getContactInfo());

        verify(iSellerRepository, times(1)).findById(existingSeller.getId());
        verify(iSellerRepository, times(1)).save(existingSeller);
        verify(sellerMapper, times(1)).toDTO(updatedSeller);
    }

    @Test
    @DisplayName("Test updateSeller - Sellers not found")
    public void testUpdateSeller_NotFound() {

        Long sellerId = 1L;
        SellerDTO updateDTO = new SellerDTO();
        updateDTO.setName("Updated Name");
        updateDTO.setContactInfo("updated@example.com");

        when(iSellerRepository.findById(sellerId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            sellerService.updateSeller(sellerId, updateDTO);
        });

        assertEquals("Seller not found with id 1", exception.getMessage());

        verify(iSellerRepository, times(1)).findById(sellerId);
        verify(iSellerRepository, never()).save(any());
        verify(sellerMapper, never()).toDTO(any());
    }



    @Test
    @DisplayName("Test deleteSeller - Success")
    public void testDeleteSeller_Success() {

        Long sellerId = 1L;
        Seller seller = new Seller();
        seller.setId(sellerId);
        seller.setName("John Doe");
        seller.setContactInfo("john@example.com");
        seller.setRegistrationDate(LocalDateTime.now());

        when(iSellerRepository.findById(sellerId)).thenReturn(Optional.of(seller));

        sellerService.deleteSeller(sellerId);

        verify(iSellerRepository, times(1)).findById(sellerId);
        verify(iSellerRepository, times(1)).delete(seller);
    }

    @Test
    @DisplayName("Test deleteSeller - Sellers not found")
    public void testDeleteSeller_NotFound() {

        Long sellerId = 1L;
        when(iSellerRepository.findById(sellerId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            sellerService.deleteSeller(sellerId);
        });

        assertEquals("Seller not found with id 1", exception.getMessage());

        verify(iSellerRepository, times(1)).findById(sellerId);
        verify(iSellerRepository, never()).delete(any());
    }
}
