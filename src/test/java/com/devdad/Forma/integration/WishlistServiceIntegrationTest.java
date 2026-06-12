package com.devdad.Forma.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import com.devdad.Forma.model.Product;
import com.devdad.Forma.model.User;
import com.devdad.Forma.model.UserPrinciple;
import com.devdad.Forma.model.Wishlist;
import com.devdad.Forma.service.WishlistService;
import com.devdad.Forma.testutil.ProductTestData;
import com.devdad.Forma.testutil.UserTestData;

/**
 * WishlistServiceIntegrationTest
 */
@DataJpaTest
@Import(WishlistService.class)
public class WishlistServiceIntegrationTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private WishlistService wishlistService;

	private User savedUser;
	private Product savedProduct;
	private Product savedProduct2;

	@BeforeEach
	void setupAuth() {
		User user = UserTestData.user();
		savedUser = entityManager.persist(user);

		savedProduct = entityManager.persist(ProductTestData.product1());
		savedProduct2 = entityManager.persist(ProductTestData.product2());

		var principle = new UserPrinciple(savedUser);

		var auth = new TestingAuthenticationToken(
				principle,
				null,
				principle.getAuthorities());

		SecurityContextHolder.getContext().setAuthentication(auth);
	}

	@Test
	void addToWishlist_shouldSuccessfullyAddProductToWishlist() {
		var result = wishlistService.addToWishlist(
				String.valueOf(savedProduct.getId()));

		assertNotNull(result);
		assertEquals(1, result.products().size());
		assertEquals(savedProduct.getId(), result.products().get(0).id());

		// NOTE: Verify it's actually persisted
		Wishlist wishlist = entityManager.find(Wishlist.class, result.id());
		assertNotNull(wishlist);
		assertEquals(1, wishlist.getProducts().size());
	}

	@Test
	void getUserWishlist_shouldReturnUsersWishlistOnSuccessfulRetrieval() {
		wishlistService.addToWishlist(
				String.valueOf(savedProduct.getId()));
		wishlistService.addToWishlist(
				String.valueOf(savedProduct2.getId()));

		var result = wishlistService.getUserWishlist();

		assertNotNull(result);
		assertEquals(2, result.size());
	}

	@Test
	void removeProductFromWishlist_shouldReturnTrueUponSuccessfulRemoval() {
		var result = wishlistService.addToWishlist(
				String.valueOf(savedProduct.getId()));

		boolean isDeleted = wishlistService.removeProductFromWishlist(String.valueOf(savedProduct.getId()));

		assertTrue(isDeleted);
		
		Wishlist wishlist = entityManager.find(Wishlist.class, result.id());
		assertNotNull(wishlist);
		assertTrue(wishlist.getProducts().isEmpty());
	}
}
