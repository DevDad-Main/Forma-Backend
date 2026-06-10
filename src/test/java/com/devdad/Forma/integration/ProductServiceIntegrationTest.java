package com.devdad.Forma.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.context.annotation.Import;

import com.devdad.Forma.model.Product;
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
}
