package com.ciandt.vastiane.googlesheetsintegration.repository;

import com.ciandt.vastiane.googlesheetsintegration.domain.Products;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductsRepository extends JpaRepository<Products, UUID> {
}
