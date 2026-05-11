package com.devdad.Forma.mapper;

import com.devdad.Forma.model.Wishlist;
import com.devdad.Forma.model.dto.wishlist.WishlistResponseDTO;

public class WishlistMapper {

	public static WishlistResponseDTO toDTO(Wishlist wishlist) {
		return new WishlistResponseDTO(
				wishlist.getId(),
				wishlist.getProducts().stream().map(ProductMapper::toDTO).toList(),
				wishlist.getUser().getId());
	}
}
