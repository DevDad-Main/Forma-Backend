package com.devdad.Forma.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

import com.devdad.Forma.model.Product;
import com.devdad.Forma.model.User;
import com.devdad.Forma.model.UserPrinciple;
import com.devdad.Forma.model.Wishlist;
import com.devdad.Forma.model.dto.product.ProductResponseDTO;
import com.devdad.Forma.model.dto.wishlist.WishlistResponseDTO;
import com.devdad.Forma.repository.ProductRepository;
import com.devdad.Forma.repository.UserRepository;
import com.devdad.Forma.repository.WishlistRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WishlistServiceTest {

	@Mock
	private WishlistRepository wishlistRepository;

	@Mock
	private ProductRepository productRepository;

	@Mock
	private Authentication authentication;

	@Mock
	private SecurityContext securityContext;

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private WishlistService wishlistService;

	private User testUser;

	private Product testProduct;

	@BeforeEach
	void setUp() {
		testUser = new User();
		testUser.setId(1);

		testProduct = new Product();
		testProduct.setId(1);

		when(securityContext.getAuthentication()).thenReturn(authentication);
		when(authentication.getPrincipal()).thenReturn(new UserPrinciple(testUser));
		SecurityContextHolder.setContext(securityContext);
	}

	@Nested
	@DisplayName("Add To Wishlist Tests.")
	class AddToWishlistTest {

		@Test
		void addToWishlist_savesAndReturnsDTO() {
			Wishlist wishlist = new Wishlist();
			wishlist.setId(1);
			wishlist.setUser(testUser);
			wishlist.setProducts(new ArrayList<>());

			when(wishlistRepository.findByUserId(1)).thenReturn(Optional.of(wishlist));
			when(productRepository.findById(1)).thenReturn(Optional.of(testProduct));
			when(wishlistRepository.save(any())).thenReturn(wishlist);

			WishlistResponseDTO result = wishlistService.addToWishlist("1");

			assertNotNull(result);
			assertEquals(1, result.products().size());
			assertEquals(1, result.id());
			verify(wishlistRepository).save(wishlist);
			verify(productRepository).findById(1);
		}
	}

	@Nested
	@DisplayName("Get Users Wishlist")
	class GetUserWishlist {
		
		@Test
		void getUserWishlist_returnsWishlistForAuthenticatedUser(){
			Product p1 = new Product();
			p1.setId(1);
			Product p2 = new Product();
			p2.setId(2);

			Wishlist wishlist = new Wishlist();
			wishlist.setId(1);
			wishlist.setUser(testUser);
			wishlist.setProducts(List.of(p1, p2));

			when(wishlistRepository.findWishlistByUser(testUser)).thenReturn(wishlist);

			List<ProductResponseDTO> userWishlist = wishlistService.getUserWishlist();

			assertNotNull(userWishlist);
			assertEquals(2, userWishlist.size());
			verify(wishlistRepository).findWishlistByUser(testUser);
		}

	}
}

