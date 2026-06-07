package com.devdad.Forma.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.Optional;

import com.devdad.Forma.exception.EmailAlreadyExistsException;
import com.devdad.Forma.model.User;
import com.devdad.Forma.model.UserPrinciple;
import com.devdad.Forma.model.dto.user.UserResponse;
import com.devdad.Forma.repository.UserRepository;
import com.devdad.Forma.testutil.UserTestData;

import jakarta.servlet.http.HttpServletResponse;

/**
 * AuthServiceTest
 */
@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private JwtService jwtService;

	@Mock
	private AuthenticationManager authenticationManager;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private Authentication authentication;

	@Mock
	private SecurityContext securityContext;

	@Mock
	HttpServletResponse response;

	@InjectMocks
	private AuthService authService;

	private User testUser;

	@BeforeEach
	void setup() {
		// testUser = User.builder()
		// .id(1)
		// .email("test@forma.com")
		// .build();

		// lenient().when(securityContext.getAuthentication()).thenReturn(authentication);
		// lenient().when(authentication.getPrincipal()).thenReturn(new
		// UserPrinciple(testUser));
		// SecurityContextHolder.setContext(securityContext);
	}

	@Nested
	@DisplayName("Get Authenticated User Test")
	class GetAuthenticatedUser {

		@Test
		void getAuthenticatedUser_shouldReturnAuthenticatedUserResponse() {
			when(securityContext.getAuthentication()).thenReturn(authentication);
			when(authentication.getPrincipal()).thenReturn(new UserPrinciple(testUser));
			SecurityContextHolder.setContext(securityContext);

			UserResponse result = authService.getAuthenticatedUser();

			assertNotNull(result);

			assertEquals(1, result.id());
		}
	}

	@Nested
	@DisplayName("Get Authenticated User Test")
	class RegisterUser {

		@Test
		void registerUser_shouldThrowEmailAlreadyExistsException() {
			when(userRepository.findByEmail("test@forma.com"))
					.thenReturn(Optional.of(UserTestData.user()));

			assertThrows(EmailAlreadyExistsException.class,
					() -> authService.registerUser(UserTestData.userRegisterResponse(), response));
		}

		@Test
		void registerUser_shouldSuccessfullyRegisterNewUser() {
			when(userRepository.save(any())).thenReturn(UserTestData.user());
			when(passwordEncoder.encode(anyString())).thenReturn("encoded-pass");
			when(jwtService.generateToken(anyString())).thenReturn("test-jwt");

			User result = authService.registerUser(UserTestData.userRegisterResponse(), response);

			// NOTE: loose matcher
			// verify(userRepository).save(any(User.class));

			// NOTE: capture and assert specific fields (better)
			ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

			verify(userRepository).save(captor.capture());

			User captured = captor.getValue();
			assertEquals("test@forma.com", captured.getEmail());
			assertEquals("Forma", captured.getFirstName());
			assertEquals("Tests", captured.getLastName());
			assertNotNull(result);
			assertEquals(1, result.getId());
		}

		@Test
		void registerUser_shouldSetJwtCookieAndToken() {
			when(userRepository.save(any())).thenReturn(UserTestData.user());
			when(passwordEncoder.encode(anyString())).thenReturn("encoded-pass");
			when(jwtService.generateToken(anyString())).thenReturn("test-jwt-token");

			User result = authService.registerUser(UserTestData.userRegisterResponse(), response);

			verify(jwtService).generateToken(String.valueOf(result.getId()));
			verify(response).setHeader(eq("Set-Cookie"), contains("test-jwt-token"));
		}

	}

}
