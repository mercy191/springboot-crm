package com.example.springboot_crm.service;

import com.example.springboot_crm.dto.SellerDTO;
import com.example.springboot_crm.dto.SellerWithTotalAmountDTO;
import com.example.springboot_crm.entity.Seller;
import com.example.springboot_crm.entity.Transaction;
import com.example.springboot_crm.mapper.SellerMapper;
import com.example.springboot_crm.repository.ISellerRepository;
import com.example.springboot_crm.exception.ResourceNotFoundException;
import com.example.springboot_crm.repository.ITransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.AbstractMap;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
public class SellerService {

    private final ITransactionRepository iTransactionRepository;
    private final ISellerRepository iSellerRepository;
    private final SellerMapper sellerMapper;

    public SellerService(ITransactionRepository iTransactionRepository, ISellerRepository iSellerRepository, SellerMapper sellerMapper) {
        this.iTransactionRepository = iTransactionRepository;
        this.iSellerRepository = iSellerRepository;
        this.sellerMapper = sellerMapper;
    }

    public List<SellerDTO> getAllSellers() {
        List<SellerDTO> sellerDTOs = iSellerRepository.findAll()
                .stream()
                .map(sellerMapper::toDTO)
                .toList();

        if (sellerDTOs.isEmpty()) {
            throw new ResourceNotFoundException("No sellers found");
        }

        return sellerDTOs;
    }

    public SellerDTO getSellerById(Long id) {
        Seller seller = iSellerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Seller not found with id " + id));

        return sellerMapper.toDTO(seller);
    }

    public SellerWithTotalAmountDTO getMostProductiveSeller(LocalDateTime start, LocalDateTime end) {
        Map.Entry<Seller, BigDecimal> mostProductiveSellerWithAmount = iSellerRepository.findAll().stream()
                .map(seller -> {
                    BigDecimal totalAmount = iTransactionRepository.findBySellerAndTransactionDateBetween(seller, start, end)
                            .stream()
                            .map(Transaction::getAmount)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    return new AbstractMap.SimpleEntry<>(seller, totalAmount);
                })
                .max(Comparator.comparing(AbstractMap.SimpleEntry::getValue))
                .orElseThrow(() -> new ResourceNotFoundException("No most productive seller found"));

        return new SellerWithTotalAmountDTO(
                sellerMapper.toDTO(mostProductiveSellerWithAmount.getKey()),
                mostProductiveSellerWithAmount.getValue()
        );
    }

    public List<SellerWithTotalAmountDTO> getSellersWithTotalAmountLessThan(BigDecimal amount, LocalDateTime start, LocalDateTime end) {
        List<AbstractMap.SimpleEntry<Seller, BigDecimal>> sellersWithTotalAmount = iSellerRepository.findAll().stream()
                .map(seller -> {
                    BigDecimal total = iTransactionRepository.findBySellerAndTransactionDateBetween(seller, start, end)
                            .stream()
                            .map(Transaction::getAmount)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    return new AbstractMap.SimpleEntry<>(seller, total);
                })
                .filter(entry -> entry.getValue().compareTo(amount) < 0)
                .toList();

        if (sellersWithTotalAmount.isEmpty()) {
            throw new ResourceNotFoundException("No sellers found with total transaction amount less than " + amount);
        }

        return sellersWithTotalAmount.stream()
                .map(entry -> new SellerWithTotalAmountDTO(
                        sellerMapper.toDTO(entry.getKey()),
                        entry.getValue())
                )
                .toList();
    }

    public SellerDTO createSeller(SellerDTO sellerDTO) {
        Seller seller = sellerMapper.toEntity(sellerDTO);
        Seller savedSeller = iSellerRepository.save(seller);
        return sellerMapper.toDTO(savedSeller);
    }

    public SellerDTO updateSeller(Long id, SellerDTO sellerDTO) {
        Seller existingSeller = iSellerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Seller not found with id " + id));

        existingSeller.setName(sellerDTO.getName());
        existingSeller.setContactInfo(sellerDTO.getContactInfo());
        // registrationDate не обновляется 
        Seller updatedSeller = iSellerRepository.save(existingSeller);
        return sellerMapper.toDTO(updatedSeller);
    }

    public void deleteSeller(Long id) {
        Seller deletedSeller = iSellerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Seller not found with id " + id));
        iSellerRepository.delete(deletedSeller);
    }
}
