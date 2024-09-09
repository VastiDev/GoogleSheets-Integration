package com.ciandt.vastiane.googlesheetsintegration.service;

import com.ciandt.vastiane.googlesheetsintegration.domain.Products;
import com.ciandt.vastiane.googlesheetsintegration.repository.ProductsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductsService {

    private final ProductsRepository productsRepository;
    private final GoogleSheetsService googleSheetsService;

    // Save a product to the database and append it to Google Sheets
    public Products save(Products product) throws IOException {
        Products savedProduct = productsRepository.save(product);

        // Prepare data for Google Sheets
        List<List<Object>> data = Collections.singletonList(
                Arrays.asList(savedProduct.getId().toString(), savedProduct.getName(), savedProduct.getPrice(), savedProduct.getQuantity())
        );

        // Append new product data to Google Sheets
        googleSheetsService.appendData("Products!A:D", data);

        return savedProduct;
    }

    // Retrieve all products from the database
    public List<Products> getAll() {
        return productsRepository.findAll();
    }

    // Get product by ID
    public Products getById(UUID id) {
        return productsRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    // Update an existing product and Google Sheets entry
    public Products updateProduct(UUID id, Products product) throws IOException {
        Products existingProduct = getById(id);

        updateProductData(existingProduct, product);
        Products updatedProduct = productsRepository.save(existingProduct);

        // Prepare updated data for Google Sheets
        List<List<Object>> data = Collections.singletonList(
                Arrays.asList(updatedProduct.getId().toString(), updatedProduct.getName(), updatedProduct.getPrice(), updatedProduct.getQuantity())
        );

        // Update data in Google Sheets (if needed, append logic can be kept as well)
        googleSheetsService.updateData("Products!A:D", data);

        return updatedProduct;
    }

    // Delete a product by ID from the database and Google Sheets
    public void deleteById(UUID id) throws IOException {
        Products product = getById(id);
        productsRepository.deleteById(id);

        // Remove product from Google Sheets
        googleSheetsService.deleteProductFromSheet(id.toString());
    }

    // Helper method to update product data in the system
    private void updateProductData(Products existingProduct, Products product) {
        existingProduct.setName(product.getName());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setQuantity(product.getQuantity());
    }
}

