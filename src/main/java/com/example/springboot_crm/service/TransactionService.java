package com.example.springboot_crm.service;

import com.example.springboot_crm.dto.TransactionDTO;
import com.example.springboot_crm.entity.Seller;
import com.example.springboot_crm.entity.Transaction;
import com.example.springboot_crm.exception.ResourceNotFoundException;
import com.example.springboot_crm.mapper.TransactionMapper;
import com.example.springboot_crm.repository.ISellerRepository;
import com.example.springboot_crm.repository.ITransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private final ITransactionRepository iTransactionRepository;
    private final ISellerRepository iSellerRepository;
    private final TransactionMapper transactionMapper;

    public TransactionService(ITransactionRepository iTransactionRepository, ISellerRepository iSellerRepository, TransactionMapper transactionMapper) {
        this.iTransactionRepository = iTransactionRepository;
        this.iSellerRepository = iSellerRepository;
        this.transactionMapper = transactionMapper;
    }

    public List<TransactionDTO> getAllTransactions() {
        List<Transaction> transactions = iTransactionRepository.findAll();
        if (transactions.isEmpty()) {
            throw new ResourceNotFoundException("No transactions found");
        }

        return transactions.stream()
                .map(transactionMapper::toDTO)
                .collect(Collectors.toList());
    }

    public TransactionDTO getTransactionById(Long id) {
        Transaction transaction = iTransactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id " + id));

        return transactionMapper.toDTO(transaction);
    }

    public List<TransactionDTO> getTransactionsBySellerId(Long sellerId) {
        List<Transaction> transactions = iTransactionRepository.findBySellerId(sellerId);
        if (transactions.isEmpty()) {
            throw new ResourceNotFoundException("No transactions found for seller with id " + sellerId);
        }

        return transactions.stream()
                .map(transactionMapper::toDTO)
                .collect(Collectors.toList());
    }

    public TransactionDTO createTransaction(Long sellerId, TransactionDTO transactionDTO) {
        Seller seller = iSellerRepository.findById(sellerId)
                .orElseThrow(() -> new ResourceNotFoundException("Seller not found with id " + sellerId));

        Transaction transaction = transactionMapper.toEntity(transactionDTO);
        transaction.setSeller(seller);
        Transaction savedTransaction = iTransactionRepository.save(transaction);
        return transactionMapper.toDTO(savedTransaction);
    }
}