package com.example.springboot_crm.controller;

import com.example.springboot_crm.dto.TransactionDTO;
import com.example.springboot_crm.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("api/springboot_crm")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionDTO>> getAllTransactions() {
        List<TransactionDTO> transactions = transactionService.getAllTransactions();
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/transactions/{id}")
    public ResponseEntity<TransactionDTO> getTransactionById(@PathVariable Long id) {
        TransactionDTO transactionDTO = transactionService.getTransactionById(id);
        return ResponseEntity.ok(transactionDTO);
    }

    @GetMapping("/transactions/sellerId/{sellerId}")
    public ResponseEntity<List<TransactionDTO>> getTransactionsBySellerId(@PathVariable Long sellerId) {
        List<TransactionDTO> transactions = transactionService.getTransactionsBySellerId(sellerId);
        return ResponseEntity.ok(transactions);
    }

    @PostMapping("/transactions")
    public ResponseEntity<TransactionDTO> createTransaction(@RequestParam Long sellerId,  @Valid @RequestBody TransactionDTO transactionDTO) {
        TransactionDTO createdTransaction = transactionService.createTransaction(sellerId, transactionDTO);
        return new ResponseEntity<>(createdTransaction, HttpStatus.CREATED);
    }
}
