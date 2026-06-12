package com.devdad.Forma.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.devdad.Forma.service.ProductService;
import com.devdad.Forma.testutil.ProductTestData;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

/**
 * ProductControllerTest
 */
@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc(addFilters = false) // NOTE: Skips JWT filter
public class ProductControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockitoBean
	private ProductService productService;

	// NOTE: ---- Public endpoint, no auth needed ---
	@Test
	void getProducts_shouldReturn200() throws Exception {
		when(productService.getProducts())
				.thenReturn(List.of(ProductTestData.productResponseDTO1()));

		mockMvc.perform(get("/api/product"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].name").value("Modern Oak Dining Table"));
	}

	@Test
	@WithMockUser(authorities = "ADMIN")
	void createProduct_shouldReturn200() throws Exception {
		when(productService.createProduct(any()))
				.thenReturn(ProductTestData.productResponseDTO1());

		mockMvc.perform(post("/api/admin/products")
				.contentType(MediaType.APPLICATION_JSON)
				.content(
						"""
								{
									"name": "Table",
									"price": 499.99,
									"image": "img.png",
									"inStock": true,
									"category": "Dining",
									"description": "Description"
								}
								"""))
				.andExpect(status().isOk());
	}

}
