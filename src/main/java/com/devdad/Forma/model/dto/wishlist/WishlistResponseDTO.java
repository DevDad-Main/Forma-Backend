package com.devdad.Forma.model.dto.wishlist;

import java.util.List;

import com.devdad.Forma.model.dto.product.ProductResponseDTO;

public record WishlistResponseDTO(
		int id,
		List<ProductResponseDTO> products,
		int userId) {
}
