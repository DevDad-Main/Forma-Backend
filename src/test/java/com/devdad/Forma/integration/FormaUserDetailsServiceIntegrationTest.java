package com.devdad.Forma.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.context.annotation.Import;

import com.devdad.Forma.model.User;
import com.devdad.Forma.service.FormaUserDetailsService;
import com.devdad.Forma.testutil.UserTestData;

@DataJpaTest
@Import(FormaUserDetailsService.class)
public class FormaUserDetailsServiceIntegrationTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private FormaUserDetailsService formaUserDetailsService;

	@Test
	void loadUserByUsername_shouldReturnUserDetailsWhenUserExistsById() {
		User user = UserTestData.user();
		entityManager.persist(user);

		var result = formaUserDetailsService.loadUserByUsername(String.valueOf(user.getId()));

		assertNotNull(result);
		assertEquals(String.valueOf(user.getId()), result.getUsername());
	}

	@Test
	void loadUserByUsername_shouldReturnUserDetailsWhenUserExistsByEmail() {
		User user = UserTestData.user();
		entityManager.persist(user);

		var result = formaUserDetailsService.loadUserByUsername(user.getEmail());

		assertNotNull(result);
		assertEquals(String.valueOf(user.getId()), result.getUsername());
	}
}
