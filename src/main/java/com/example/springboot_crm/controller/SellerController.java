package com.example.springboot_crm.controller;

import com.example.springboot_crm.dto.SellerDTO;
import com.example.springboot_crm.dto.SellerWithTotalAmountDTO;
import com.example.springboot_crm.service.SellerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("api/springboot_crm")
public class SellerController {

    private final SellerService sellerService;

    @Autowired
    public SellerController(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    @GetMapping("/sellers")
    public ResponseEntity<List<SellerDTO>> getAllSellers() {
        List<SellerDTO> sellers = sellerService.getAllSellers();
        return ResponseEntity.ok(sellers);
    }

    @GetMapping("/sellers/{id}")
    public ResponseEntity<SellerDTO> getSellerById(@PathVariable Long id) {
        SellerDTO sellerDTO = sellerService.getSellerById(id);
        return ResponseEntity.ok(sellerDTO);
    }

    @GetMapping("/sellers/mostProductive")
    public ResponseEntity<SellerWithTotalAmountDTO> getMostProductiveSeller(@RequestParam LocalDateTime start, @RequestParam LocalDateTime end) {
        SellerWithTotalAmountDTO seller = sellerService.getMostProductiveSeller(start, end);
        return ResponseEntity.ok(seller);
    }

    @GetMapping("/sellers/withTotalAmountLessThan")
    public ResponseEntity<List<SellerWithTotalAmountDTO>> getSellersWithTotalAmountLessThan(@RequestParam BigDecimal amount, @RequestParam LocalDateTime start, @RequestParam LocalDateTime end) {
        List<SellerWithTotalAmountDTO> sellers = sellerService.getSellersWithTotalAmountLessThan(amount, start, end);
        return ResponseEntity.ok(sellers);
    }

    @PostMapping("/sellers")
    public ResponseEntity<SellerDTO> createSeller(@Valid @RequestBody SellerDTO sellerDTO) {
        SellerDTO createdSeller = sellerService.createSeller(sellerDTO);
        return new ResponseEntity<>(createdSeller, HttpStatus.CREATED);
    }

    @PutMapping(path = "/sellers/{id}")
    public ResponseEntity<SellerDTO> updateSeller(@PathVariable Long id, @Valid @RequestBody SellerDTO sellerDTO) {
        SellerDTO updatedSeller = sellerService.updateSeller(id, sellerDTO);
        return ResponseEntity.ok(updatedSeller);
    }

    @DeleteMapping(value = "/sellers/{id}")
    public ResponseEntity<Void> deleteSeller(@PathVariable Long id) {
        sellerService.deleteSeller(id);
        return ResponseEntity.noContent().build();
    }
}
