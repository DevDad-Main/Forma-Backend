package com.devdad.Forma.service;

import java.nio.file.InvalidPathException;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.devdad.Forma.exception.ResourceNotFoundException;
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

    public Product updateProduct(int id, Product product) {
        Product existingProduct = productRepository
                .findById(id)
                .orElseGet(() -> createProduct(product));

        // copies all fields from 'product' to 'existingProduct'
        // We can specify fields at the end to ignore.
        BeanUtils.copyProperties(product, existingProduct, "id");
        return productRepository.save(existingProduct);
    }

    public Product getProductById(String id) {
        try {
            Integer numericId = Integer.valueOf(id);
            return productRepository.findById(numericId)
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + numericId));
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID must be a numeric value.");
        }
    }
}
