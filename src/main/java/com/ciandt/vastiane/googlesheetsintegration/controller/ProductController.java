package com.ciandt.vastiane.googlesheetsintegration.controller;

import com.ciandt.vastiane.googlesheetsintegration.domain.Products;
import com.ciandt.vastiane.googlesheetsintegration.service.ProductsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductsService productService;

    // Create a new product and save it to the repository and Google Sheets
    @PostMapping
    public ResponseEntity<Products> addProduct(@RequestBody Products product) throws IOException {
        Products newProduct = productService.save(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(newProduct);
    }

    // Retrieve all products from the repository
    @GetMapping
    public ResponseEntity<List<Products>> getAllProducts() {
        List<Products> products = productService.getAll();
        return ResponseEntity.ok(products);
    }

    // Retrieve a product by its ID
    @GetMapping("/{id}")
    public ResponseEntity<Products> getProductById(@PathVariable UUID id) {
        Products product = productService.getById(id);
        return ResponseEntity.ok(product);
    }

    // Update an existing product by its ID and save changes to Google Sheets
    @PutMapping("/{id}")
    public ResponseEntity<Products> updateProduct(@PathVariable UUID id, @RequestBody Products product) throws IOException {
        Products updatedProduct = productService.updateProduct(id, product);
        return ResponseEntity.ok(updatedProduct);
    }

    // Delete a product by its ID from both the repository and Google Sheets
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) throws IOException {
        productService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
