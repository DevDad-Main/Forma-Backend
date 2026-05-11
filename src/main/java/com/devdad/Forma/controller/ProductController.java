package com.devdad.Forma.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devdad.Forma.model.dto.product.ProductCreateRequestDTO;
import com.devdad.Forma.model.dto.product.ProductResponseDTO;
import com.devdad.Forma.service.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class ProductController {

	@Autowired
	private ProductService productService;

	@PreAuthorize("hasAuthority('ADMIN')")
	@PostMapping("/admin/products")
	public ResponseEntity<ProductResponseDTO> createProduct(@Valid @RequestBody ProductCreateRequestDTO dto) {
		return ResponseEntity.ok(productService.createProduct(dto));
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@PutMapping("/admin/products/{id}")
	public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable int id,
			@Valid @RequestBody ProductCreateRequestDTO dto) {
		return ResponseEntity.ok(productService.updateProduct(id, dto));
	}

	@GetMapping("/products")
	public ResponseEntity<List<ProductResponseDTO>> getProducts() {
		List<ProductResponseDTO> products = productService.getProducts();
		return ResponseEntity.ok(products);
	}

	@GetMapping("/products/{id}")
	public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable String id) {
		return ResponseEntity.ok(productService.getProductById(id));
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@PostMapping("/admin/products/seed")
	public ResponseEntity<?> seedDummyProducts(@Valid @RequestBody List<ProductCreateRequestDTO> dtos) {
		try {
			List<ProductResponseDTO> seededProducts = productService.saveProducts(dtos);
			return ResponseEntity.ok(seededProducts);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body("Error: " + e.getMessage());
		}
	}

}
