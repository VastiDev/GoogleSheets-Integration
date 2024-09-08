package com.ciandt.vastiane.googlesheetsintegration.controller;
;
import com.ciandt.vastiane.googlesheetsintegration.domain.Products;
import com.ciandt.vastiane.googlesheetsintegration.service.ProductsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductsService productService;

    @PostMapping
    public ResponseEntity<Products> addProduct(@RequestBody Products product) {
        Products newProduct = productService.save(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(newProduct);
    }

    @GetMapping
    public ResponseEntity<List<Products>> getAllProducts() {
        List<Products> products = productService.getAll();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Products> getProductById(@PathVariable UUID id) {
        Products product = productService.getById(id);
        return ResponseEntity.ok(product);

    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Products> updateProduct(@PathVariable UUID id, @RequestBody Products product) {
        Products existingProduct = productService.getById(id);
        updateDatas(product, existingProduct);
        productService.save(existingProduct);
        return ResponseEntity.ok(existingProduct);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        Products product = productService.getById(id);
        productService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    private static void updateDatas(Products product, Products existingProduct) {
        existingProduct.setName(product.getName());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setQuantity(product.getQuantity());
    }
}

