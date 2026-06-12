package com.devdad.Forma.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import com.devdad.Forma.model.User;
import com.devdad.Forma.model.UserPrinciple;
import com.devdad.Forma.model.dto.user.UserRegisterResponse;
import com.devdad.Forma.service.AuthService;
import com.devdad.Forma.service.JwtService;
import com.devdad.Forma.testutil.UserTestData;

import jakarta.servlet.http.HttpServletResponse;

@DataJpaTest
@Import({ AuthService.class, AuthServiceIntegrationTest.TestConfig.class })
public class AuthServiceIntegrationTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private AuthService authService;

	private User savedUser;
	private HttpServletResponse response;

	@TestConfiguration
	static class TestConfig {

		@Bean
		JwtService jwtService() {
			JwtService mock = mock(JwtService.class);
			when(mock.generateToken(anyString())).thenReturn("test-jwt-token");
			return mock;
		}

		@Bean
		AuthenticationManager authenticationManager() {
			return mock(AuthenticationManager.class);
		}

		@Bean
		PasswordEncoder passwordEncoder() {
			return new BCryptPasswordEncoder(12);
		}
	}

	@BeforeEach
	void setUp() {
		response = mock(HttpServletResponse.class);

		User user = UserTestData.user();
		savedUser = entityManager.persist(user);

		var principle = new UserPrinciple(savedUser);
		var auth = new TestingAuthenticationToken(
				principle,
				null,
				principle.getAuthorities());

		SecurityContextHolder.getContext().setAuthentication(auth);
	}

	@Test
	void getAuthenticatedUser_shouldReturnAuthenticatedUserFromSecurityContext() {
		var result = authService.getAuthenticatedUser();

		assertNotNull(result);
		assertEquals(savedUser.getEmail(), result.email());
		assertEquals(savedUser.getFirstName(), result.firstName());
	}

	@Test
	void registerUser_shouldPersistUserAndReturnUserWithId() {
		var registerDTO = UserTestData.userRegisterResponse();
		// Use a different email from the one persisted in setUp
		var dto = new UserRegisterResponse(
				"newuser@forma.com",
				registerDTO.firstName(),
				registerDTO.lastName(),
				registerDTO.password());

		User result = authService.registerUser(dto, response);

		assertNotNull(result);
		assertNotNull(result.getId());

		User persisted = entityManager.find(User.class, result.getId());
		assertNotNull(persisted);
		assertEquals("newuser@forma.com", persisted.getEmail());
	}

}
