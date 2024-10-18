package com.example.springboot_crm.repository;

import com.example.springboot_crm.entity.Seller;
import com.example.springboot_crm.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ITransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findBySellerId(Long sellerId);
    List<Transaction> findBySellerAndTransactionDateBetween(Seller seller, LocalDateTime start, LocalDateTime end);
}
