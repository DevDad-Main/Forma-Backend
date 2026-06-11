package com.devdad.Forma.integration;

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

	@BeforeEach
	void setupAuth() {
		User user = UserTestData.user();
		savedUser = entityManager.persist(user);

		savedProduct = entityManager.persist(ProductTestData.product1());

		var principle = new UserPrinciple(savedUser);

		var auth = new TestingAuthenticationToken(
				principle,
				null,
				principle.getAuthorities());

		SecurityContextHolder.getContext().setAuthentication(auth);
	}

	@Test
	void addToWishlist_shouldSuccessfullyAddProductToWishlist() {
	}

}
