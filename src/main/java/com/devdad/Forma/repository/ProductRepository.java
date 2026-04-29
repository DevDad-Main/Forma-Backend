package com.devdad.Forma.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devdad.Forma.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

}
