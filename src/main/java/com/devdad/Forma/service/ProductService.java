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
import com.devdad.Forma.mapper.ProductMapper;
import com.devdad.Forma.model.Product;
import com.devdad.Forma.model.dto.product.ProductCreateRequestDTO;
import com.devdad.Forma.model.dto.product.ProductResponseDTO;
import com.devdad.Forma.repository.ProductRepository;

@Service
public class ProductService {

	@Autowired
	private ProductRepository productRepository;

	public ProductResponseDTO createProduct(ProductCreateRequestDTO dto) {
		Product product = ProductMapper.toEntity(dto);
		product = productRepository.save(product);
		return ProductMapper.toDTO(product);
	}

	public List<ProductResponseDTO> getProducts() {
		return productRepository.findAll()
				.stream()
				.map(ProductMapper::toDTO)
				.toList();
	}

	public List<ProductResponseDTO> saveProducts(List<ProductCreateRequestDTO> dtos) {
		List<Product> products = dtos.stream()
				.map(dto -> {
					Product product = ProductMapper.toEntity(dto);
					product.setId(0);
					return product;
				})
				.toList();
		return productRepository.saveAll(products)
				.stream()
				.map(ProductMapper::toDTO)
				.toList();
	}

	public ProductResponseDTO updateProduct(int id, ProductCreateRequestDTO dto) {
		Product existingProduct = productRepository
				.findById(id)
				.orElseGet(() -> ProductMapper.toEntity(dto));

		Product updatedProduct = ProductMapper.toEntity(dto);
		BeanUtils.copyProperties(updatedProduct, existingProduct, "id");
		existingProduct = productRepository.save(existingProduct);
		return ProductMapper.toDTO(existingProduct);
	}

	public ProductResponseDTO getProductById(String id) {
		try {
			Integer numericId = Integer.valueOf(id);
			Product product = productRepository.findById(numericId)
					.orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + numericId));
			return ProductMapper.toDTO(product);
		} catch (NumberFormatException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID must be a numeric value.");
		}
	}
}
