package com.ciandt.vastiane.googlesheetsintegration.service;

import com.ciandt.vastiane.googlesheetsintegration.domain.Products;
import com.ciandt.vastiane.googlesheetsintegration.repository.ProductsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductsService {
    
    private final ProductsRepository productsRepository;
    
    public Products save(Products product) {
        productsRepository.save(product);
        return product;
    }
    public List<Products> getAll() {
        return productsRepository.findAll();
    }
    public Products getById(UUID id) {
        Products product = productsRepository.findById(id).orElseThrow(()-> new RuntimeException("id not found"));
        return product;
    }

    public void deleteById(UUID id) {
        productsRepository.deleteById(id);
    }
}
