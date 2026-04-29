package com.devdad.Forma.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.devdad.Forma.model.Product;
import com.devdad.Forma.repository.ProductRepository;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public List<Product> getProducts() {
        // Check if current user is admin (Alternative to
        // @PreAuthorize("hasAuthority('ADMIN')"))
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) {
            throw new AccessDeniedException("Admin access required.");
        }

        return productRepository.findAll();
    }

    public List<Product> saveProducts(List<Product> products) {
        for (Product product : products) {
            // Clear ID so DB gernerates new ones.
            // Due to frontend sending ids and merge issues
            product.setId(0);
        }
        return productRepository.saveAll(products);
    }
}
