package com.springboot.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.springboot.demo.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}