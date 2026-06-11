package com.devdad.Forma.mapper;

import java.util.ArrayList;

import com.devdad.Forma.model.Product;
import com.devdad.Forma.model.dto.product.ProductCreateRequestDTO;
import com.devdad.Forma.model.dto.product.ProductResponseDTO;

public class ProductMapper {

	public static ProductResponseDTO toDTO(Product product) {
		return new ProductResponseDTO(
				product.getId(),
				product.getName(),
				product.getPrice(),
				product.getOriginalPrice(),
				product.getImage(),
				product.getHoverImage(),
				product.getDimensions(),
				product.getTags(),
				product.getInStock(),
				product.getIsNew(),
				product.getIsBestSeller(),
				product.getCategory(),
				product.getMaterial(),
				product.getColor(),
				product.getDescription());
	}

	public static Product toEntity(ProductCreateRequestDTO dto) {
		Product product = new Product();
		product.setName(dto.name());
		product.setPrice(dto.price());
		product.setOriginalPrice(dto.originalPrice());
		product.setImage(dto.image());
		product.setHoverImage(dto.hoverImage());
		product.setDimensions(dto.dimensions());
		product.setTags(dto.tags() == null ? null : new ArrayList<>(dto.tags()));
		product.setInStock(dto.inStock());
		product.setIsNew(dto.isNew());
		product.setIsBestSeller(dto.isBestSeller());
		product.setCategory(dto.category());
		product.setMaterial(dto.material());
		product.setColor(dto.color());
		product.setDescription(dto.description());
		return product;
	}
}
