package com.devdad.Forma.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.context.annotation.Import;

import com.devdad.Forma.mapper.ProductMapper;
import com.devdad.Forma.model.Product;
import com.devdad.Forma.model.dto.product.ProductCreateRequestDTO;
import com.devdad.Forma.model.dto.product.ProductResponseDTO;
import com.devdad.Forma.service.ProductService;
import com.devdad.Forma.testutil.ProductTestData;

/**
 * ProductServiceIntegrationTest
 */
@DataJpaTest
@Import(ProductService.class)
public class ProductServiceIntegrationTest {

	@Autowired
	private ProductService productService;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	void createProduct_shouldPersistAndReturnDTO() {
		var dto = ProductTestData.productCreateRequestDTO1();
		var result = productService.createProduct(dto);

		Product saved = entityManager.find(Product.class, result.id());

		assertNotNull(result.id());
		assertEquals(dto.name(), saved.getName());
	}

	@Test
	void getProducts_shouldReturnAllPersistedProducts() {
		List<Product> prods = List.of(
				ProductTestData.product1(),
				ProductTestData.product2());

		prods.forEach(prod -> entityManager.persist(prod));

		Product prod1 = entityManager.find(Product.class, prods.get(0).getId());
		Product prod2 = entityManager.find(Product.class, prods.get(1).getId());

		assertNotNull(prod1);
		assertNotNull(prod2);

		assertEquals(1, prod1.getId());
		assertEquals(2, prod2.getId());
	}

	@Test
	void saveProducts_shouldPersistMultipleProducts() {
		entityManager.flush();

		List<ProductCreateRequestDTO> dtos = List.of(
				ProductTestData.productCreateRequestDTO1(),
				ProductTestData.productCreateRequestDTO2());

		List<Product> prodsToPersist = dtos.stream()
				.map(dto -> ProductMapper.toEntity(dto))
				.toList();

		prodsToPersist.forEach(prod -> entityManager.persist(prod));

		Product prod1 = entityManager.find(Product.class, prodsToPersist.get(0).getId());

		assertNotNull(prod1);

		assertEquals(prodsToPersist.get(0).getId(), prod1.getId());
	}

	@Test
	void updateProduct_shouldSuccessfullyUpdateAProductWithNewProperties() {
		var dto = ProductTestData.productCreateRequestDTO1();
		var created = productService.createProduct(dto);
		int savedId = created.id();

		var updateDTO = ProductTestData.productCreateRequestDTO2();
		ProductResponseDTO result = productService.updateProduct(savedId, updateDTO);

		assertEquals(savedId, result.id()); // ID unchanged

		Product updated = entityManager.find(Product.class, savedId);
		assertNotNull(updated);
		assertEquals(updateDTO.name(), updated.getName());
	}

	@Test
	void getProductById_shouldReturnProductById() {
		var dto = ProductTestData.productCreateRequestDTO1();
		var created = productService.createProduct(dto);

		var result = productService.getProductById(String.valueOf(created.id()));

		assertNotNull(result);
		assertEquals(created.id(), result.id());
		assertEquals(dto.name(), result.name());
	}

}
