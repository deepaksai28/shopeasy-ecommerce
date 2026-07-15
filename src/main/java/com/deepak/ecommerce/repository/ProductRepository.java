package com.deepak.ecommerce.repository;

import com.deepak.ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * By extending JpaRepository<Product, Long>, we get findAll(), findById(),
 * save(), deleteById() etc. for free - Spring generates the implementation
 * at runtime. This is the "no-boilerplate CRUD" part of Spring Data JPA
 * that's worth being able to explain: you never write SQL for basic
 * operations, Spring builds the queries from the method signature/interface.
 */
public interface ProductRepository extends JpaRepository<Product, Long> {
}
