package com.devdad.Forma.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

import com.devdad.Forma.exception.ResourceNotFoundException;
import com.devdad.Forma.mapper.ProductMapper;
import com.devdad.Forma.model.Product;
import com.devdad.Forma.model.User;
import com.devdad.Forma.model.UserPrinciple;
import com.devdad.Forma.model.dto.product.ProductCreateRequestDTO;
import com.devdad.Forma.model.dto.product.ProductResponseDTO;
import com.devdad.Forma.repository.ProductRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * ProductServiceTests
 */
@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

	@Mock
	private ProductRepository productRepository;

	@Mock
	private Authentication authentication;

	@Mock
	private SecurityContext securityContext;

	@InjectMocks
	private ProductService productService;

	private User testUser;
	private Product testProduct1;
	private Product testProduct2;

	@BeforeEach
	void setupProduct() {
		testProduct1 = new Product(
				0, // ID
				"Modern Oak Dining Table", // name
				499.99, // price
				649.99, // originalPrice
				"/images/products/dining-table.jpg", // image
				"/images/products/dining-table-hover.jpg", // hoverImage
				"180x90x75 cm", // dimensions
				List.of("furniture", "dining", "wood"), // tags
				true, // inStock
				true, // isNew
				false, // isBestSeller
				"Dining Room", // category
				"Solid Oak Wood", // material
				"Natural Oak", // color
				"A premium solid oak dining table with a natural finish, perfect for modern and rustic interiors.");

		testProduct2 = new Product(
				1, // ID
				"Modern Oak Dining Chair", // name
				499.99, // price
				649.99, // originalPrice
				"/images/products/dining-table.jpg", // image
				"/images/products/dining-table-hover.jpg", // hoverImage
				"180x90x75 cm", // dimensions
				List.of("furniture", "dining", "wood"), // tags
				true, // inStock
				true, // isNew
				false, // isBestSeller
				"Dining Room", // category
				"Solid Oak Wood", // material
				"Natural Oak", // color
				"A premium solid oak dining table with a natural finish, perfect for modern and rustic interiors.");

		testUser = new User();
		testUser.setId(1);

		// when(securityContext.getAuthentication()).thenReturn(authentication);
		// when(authentication.getPrincipal()).thenReturn(new UserPrinciple(testUser));
		SecurityContextHolder.setContext(securityContext);
	}

	@Nested
	@DisplayName("Create New Product Test")
	class CreateProduct {

		@Test
		void createProduct_shouldSuccessfullyCreateANewProduct() {
			ProductCreateRequestDTO dto = new ProductCreateRequestDTO(
					"Modern Oak Dining Table", // name
					499.99, // price
					649.99, // originalPrice
					"/images/products/dining-table.jpg", // image
					"/images/products/dining-table-hover.jpg", // hoverImage
					"180x90x75 cm", // dimensions
					List.of("furniture", "dining", "wood"), // tags
					true, // inStock
					true, // isNew
					false, // isBestSeller
					"Dining Room", // category
					"Solid Oak Wood", // material
					"Natural Oak", // color
					"A premium solid oak dining table with a natural finish, perfect for modern and rustic interiors.");

			when(productRepository.save(any())).thenReturn(testProduct1);

			ProductResponseDTO result = productService.createProduct(dto);

			assertNotNull(result);
			assertEquals(0, result.id());
			verify(productRepository).save(testProduct1);
		}
	}

	@Nested
	@DisplayName("Get Products Tests")
	class GetProducts {

		@Test
		void getProducts_shouldReturnAllProducts() {
			when(productRepository.findAll()).thenReturn(List.of(testProduct1, testProduct2));

			List<ProductResponseDTO> results = productService.getProducts();

			assertNotNull(results);
			assertEquals(2, results.size());
			verify(productRepository).findAll();
		}

		@Test
		void getProductById_shouldReturnAProductById() {
			when(productRepository.findById(0)).thenReturn(Optional.of(testProduct1));

			ProductResponseDTO result = productService.getProductById("0");

			assertNotNull(result);
			assertEquals(0, result.id());
			verify(productRepository).findById(0);
		}

		@Test
		void getProductById_shouldThrowResourceNotFoundWhenProductDoesNotExist() {
			when(productRepository.findById(999)).thenReturn(Optional.empty());
			assertThrows(ResourceNotFoundException.class,
					() -> productService.getProductById("999"));
		}

		@Test
		void getProductById_shouldThrowBadRequestWhenIdIsNotNumeric() {
			assertThrows(ResponseStatusException.class,
					() -> productService.getProductById("abc"));
		}
	}

	@Nested
	@DisplayName("Update Product Test")
	class UpdateProduct {

		@Test
		void updateProduct_shouldSuccessfullyUpdateAnExisitingProduct(){
			ProductCreateRequestDTO dto = new ProductCreateRequestDTO(
					"Modern Oak Dining Table", // name
					499.99, // price
					649.99, // originalPrice
					"/images/products/dining-table.jpg", // image
					"/images/products/dining-table-hover.jpg", // hoverImage
					"180x90x75 cm", // dimensions
					List.of("furniture", "dining", "wood"), // tags
					true, // inStock
					true, // isNew
					false, // isBestSeller
					"Dining Room", // category
					"Solid Oak Wood", // material
					"Natural Oak", // color
					"A premium solid oak dining table with a natural finish, perfect for modern and rustic interiors.");

			when(productRepository.findById(0)).thenReturn(Optional.of(testProduct1));
			when(productRepository.save(any())).thenReturn(testProduct1);
			
			ProductResponseDTO result = productService.updateProduct(0, dto);

			assertNotNull(result);
			assertEquals(0, result.id());
			verify(productRepository).save(any(Product.class));
		}
	}

	@Nested
	@DisplayName("Save Products Seed Test")
	public class SaveProduct {

		@Test
		void saveProducts_shouldSaveAllProductRequestDTOsPassedIn(){
			ProductCreateRequestDTO dto = new ProductCreateRequestDTO(
					"Modern Oak Dining Table", // name
					499.99, // price
					649.99, // originalPrice
					"/images/products/dining-table.jpg", // image
					"/images/products/dining-table-hover.jpg", // hoverImage
					"180x90x75 cm", // dimensions
					List.of("furniture", "dining", "wood"), // tags
					true, // inStock
					true, // isNew
					false, // isBestSeller
					"Dining Room", // category
					"Solid Oak Wood", // material
					"Natural Oak", // color
					"A premium solid oak dining table with a natural finish, perfect for modern and rustic interiors.");
			ProductCreateRequestDTO dto2 = new ProductCreateRequestDTO(
					"Modern Oak Dining Table", // name
					499.99, // price
					649.99, // originalPrice
					"/images/products/dining-table.jpg", // image
					"/images/products/dining-table-hover.jpg", // hoverImage
					"180x90x75 cm", // dimensions
					List.of("furniture", "dining", "wood"), // tags
					true, // inStock
					true, // isNew
					false, // isBestSeller
					"Dining Room", // category
					"Solid Oak Wood", // material
					"Natural Oak", // color
					"A premium solid oak dining table with a natural finish, perfect for modern and rustic interiors.");
			List<ProductCreateRequestDTO> dtos = List.of(dto, dto2);

			when(productRepository.saveAll(anyList())).thenReturn(List.of(testProduct1, testProduct2));

			List<ProductResponseDTO> results = productService.saveProducts(dtos);
			
			assertNotNull(results);
			assertEquals(2, results.size());
			assertEquals(0, results.get(0).id());
			verify(productRepository).saveAll(anyList());
		}
	}
}
