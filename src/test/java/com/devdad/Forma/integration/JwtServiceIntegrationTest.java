package com.devdad.Forma.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Base64;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import com.devdad.Forma.config.JwtProperties;
import com.devdad.Forma.model.User;
import com.devdad.Forma.model.UserPrinciple;
import com.devdad.Forma.service.JwtService;
import com.devdad.Forma.testutil.UserTestData;

/**
 * JwtServiceIntegrationTest
 */
@SpringBootTest(classes = { JwtService.class, JwtProperties.class })
public class JwtServiceIntegrationTest {

	@Autowired
	private JwtService jwtService;

	@DynamicPropertySource
	static void overrideJwtSecret(DynamicPropertyRegistry registry) {
		registry.add("jwt.secret", () -> Base64.getEncoder().encodeToString(
				"ThisIsASecretKeyForDevelopmentOnlyThatIsExactly256Bits!!".getBytes()));

		registry.add("jwt.expiration", () -> 3600000l);
	}

	@Test
	void generateToken_shouldProduceValidToken() {
		String token = jwtService.generateToken("1");
		String userId = jwtService.extractUserId(token);

		assertEquals("1", userId);
	}

	@Test
	void validateToken_shouldReturnTrueForMatchingUser() {
		String token = jwtService.generateToken("1");
		UserDetails userDetails = new UserPrinciple(UserTestData.user());

		assertTrue(jwtService.validateToken(token, userDetails));
	}

	@Test
	void validateToken_shouldReturnFalseForNonMatchingUser() {
		String token = jwtService.generateToken("1");
		User user = UserTestData.user();
		user.setId(2); // Change the ID so it dosent match test data.
		UserDetails userDetails = new UserPrinciple(user);

		assertFalse(jwtService.validateToken(token, userDetails));
	}

	@Test
	void extractUserId_shouldThrowMalformedToken() {
		assertThrows(Exception.class,
				() -> jwtService.extractUserId("invalid.jwt.token"));
	}
}
