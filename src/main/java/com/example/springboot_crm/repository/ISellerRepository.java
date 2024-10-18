package com.example.springboot_crm.repository;

import com.example.springboot_crm.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISellerRepository extends JpaRepository<Seller, Long> { }
