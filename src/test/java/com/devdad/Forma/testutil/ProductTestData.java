package com.devdad.Forma.testutil;

import java.util.ArrayList;
import java.util.List;

import com.devdad.Forma.model.Product;
import com.devdad.Forma.model.dto.product.ProductCreateRequestDTO;
import com.devdad.Forma.model.dto.product.ProductResponseDTO;

/**
 * ProductTestData
 */
public class ProductTestData {

	public static Product product1() {
		return Product.builder()
				.name("Modern Oak Dining Table")
				.price(499.99)
				.originalPrice(649.99)
				.image("/images/products/dining-table.jpg")
				.hoverImage("/images/products/dining-table-hover.jpg")
				.dimensions("180x90x75 cm")
				// NOTE: Fixes immutability of List.of()
				.tags(new ArrayList<>(List.of("furniture", "dining", "wood")))
				.inStock(true)
				.isNew(true)
				.isBestSeller(false)
				.category("Dining Room")
				.material("Solid Oak Wood")
				.color("Natural Oak")
				.description("A premium solid oak dining table with a natural finish, perfect for modern and rustic interiors.")
				.build();
	}

	public static Product product2() {
		return Product.builder()
				.name("Modern Oak Dining Table")
				.price(499.99)
				.originalPrice(649.99)
				.image("/images/products/dining-table.jpg")
				.hoverImage("/images/products/dining-table-hover.jpg")
				.dimensions("180x90x75 cm")
				// NOTE: Fixes immutability of List.of()
				.tags(new ArrayList<>(List.of("furniture", "dining", "wood")))
				.inStock(true)
				.isNew(true)
				.isBestSeller(false)
				.category("Dining Room")
				.material("Solid Oak Wood")
				.color("Natural Oak")
				.description("A premium solid oak dining table with a natural finish, perfect for modern and rustic interiors.")
				.build();
	}

	public static ProductCreateRequestDTO productCreateRequestDTO1() {
		return new ProductCreateRequestDTO(
				"Modern Oak Dining Table",
				499.99,
				649.99,
				"/images/products/dining-table.jpg",
				"/images/products/dining-table-hover.jpg",
				"180x90x75 cm",
				List.of("furniture", "dining", "wood"),
				true,
				true,
				false,
				"Dining Room",
				"Solid Oak Wood",
				"Natural Oak",
				"A premium solid oak dining table with a natural finish, perfect for modern and rustic interiors.");
	}

	public static ProductCreateRequestDTO productCreateRequestDTO2() {
		return new ProductCreateRequestDTO(
				"Modern Oak Dining Table",
				499.99,
				649.99,
				"/images/products/dining-table.jpg",
				"/images/products/dining-table-hover.jpg",
				"180x90x75 cm",
				List.of("furniture", "dining", "wood"),
				true,
				true,
				false,
				"Dining Room",
				"Solid Oak Wood",
				"Natural Oak",
				"A premium solid oak dining table with a natural finish, perfect for modern and rustic interiors.");
	}

	public static ProductResponseDTO productResponseDTO1() {
		return new ProductResponseDTO(
				0,
				"Modern Oak Dining Table",
				499.99,
				649.99,
				"/images/products/dining-table.jpg",
				"/images/products/dining-table-hover.jpg",
				"180x90x75 cm",
				List.of("furniture", "dining", "wood"),
				true,
				true,
				false,
				"Dining Room",
				"Solid Oak Wood",
				"Natural Oak",
				"A premium solid oak dining table with a natural finish, perfect for modern and rustic interiors.");
	}

	public static ProductResponseDTO productResponseDTO2() {
		return new ProductResponseDTO(
				1,
				"Modern Oak Dining Table",
				499.99,
				649.99,
				"/images/products/dining-table.jpg",
				"/images/products/dining-table-hover.jpg",
				"180x90x75 cm",
				List.of("furniture", "dining", "wood"),
				true,
				true,
				false,
				"Dining Room",
				"Solid Oak Wood",
				"Natural Oak",
				"A premium solid oak dining table with a natural finish, perfect for modern and rustic interiors.");
	}
}
