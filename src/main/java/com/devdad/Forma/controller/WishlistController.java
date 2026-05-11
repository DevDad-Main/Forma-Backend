package com.devdad.Forma.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devdad.Forma.model.dto.product.ProductResponseDTO;
import com.devdad.Forma.model.dto.wishlist.WishlistResponseDTO;
import com.devdad.Forma.service.WishlistService;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {

	@Autowired
	private WishlistService wishlistService;

	@GetMapping
	public ResponseEntity<List<ProductResponseDTO>> getUserWishlist() {
		return ResponseEntity.ok(wishlistService.getUserWishlist());
	}

	@PostMapping("/add/{id}")
	public ResponseEntity<WishlistResponseDTO> addProductToWishlist(@PathVariable String id) {
		return ResponseEntity.ok(wishlistService.addToWishlist(id));
	}

	@DeleteMapping("/remove/{id}")
	public ResponseEntity<Boolean> removeProductFromWishlist(@PathVariable String id) {
		return ResponseEntity.ok(wishlistService.removeProductFromWishlist(id));
	}
}
