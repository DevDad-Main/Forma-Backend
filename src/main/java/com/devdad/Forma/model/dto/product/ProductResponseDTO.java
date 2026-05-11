package com.devdad.Forma.model.dto.product;

import java.util.List;

public record ProductResponseDTO(
		int id,
		String name,
		Double price,
		Double originalPrice,
		String image,
		String hoverImage,
		String dimensions,
		List<String> tags,
		Boolean inStock,
		Boolean isNew,
		Boolean isBestSeller,
		String category,
		String material,
		String color,
		String description) {
}
