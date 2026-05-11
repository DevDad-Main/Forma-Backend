package com.devdad.Forma.model.dto.product;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductCreateRequestDTO(

		@NotBlank(message = "Name is required") String name,

		@NotNull(message = "Price is required") Double price,

		Double originalPrice,

		@NotBlank(message = "Image is required") String image,

		String hoverImage,

		String dimensions,

		List<String> tags,

		@NotNull(message = "In stock status is required") Boolean inStock,

		Boolean isNew,

		Boolean isBestSeller,

		@NotBlank(message = "Category is required") String category,

		String material,

		String color,

		@NotBlank(message = "Description is required") String description) {
}
