package com.devdad.Forma.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.devdad.Forma.repository.UserRepository;
import com.devdad.Forma.testutil.UserTestData;

/**
 * FormaUserDetailsServiceTest
 */
@ExtendWith(MockitoExtension.class)
public class FormaUserDetailsServiceTest {

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private FormaUserDetailsService formaUserDetailsService;

	@Nested
	@DisplayName("Load User By Username Test")
	class LoadUserByUsername {

		@Test
		void loadUserByUsername_shouldReturnUserByEmail() {
			when(userRepository.findByEmail("test@forma.com")).thenReturn(Optional.of(UserTestData.user()));

			UserDetails result = formaUserDetailsService.loadUserByUsername("test@forma.com");

			assertNotNull(result);

			verify(userRepository).findByEmail("test@forma.com");
		}

		@Test
		void loadUserByUsername_shouldReturnUserById() {
			when(userRepository.findById(1)).thenReturn(Optional.of(UserTestData.user()));

			UserDetails result = formaUserDetailsService.loadUserByUsername("1");

			assertNotNull(result);
			assertEquals("1", result.getUsername());

			verify(userRepository).findById(1);
		}

		@Test
		void loadUserByUsername_shouldThrowUsernameNotFoundException() {
			when(userRepository.findById(1)).thenReturn(Optional.empty());

			assertThrows(UsernameNotFoundException.class,
					() -> formaUserDetailsService.loadUserByUsername("1"));
		}

		@Test
		void loadUserByUsername_shouldThrowWithUnknownEmail() {
			when(userRepository.findByEmail("unknown@test.com")).thenReturn(Optional.empty());
			assertThrows(UsernameNotFoundException.class,
					() -> formaUserDetailsService.loadUserByUsername("unknown@test.com"));
		}

		@Test
		void loadUserByUsername_shouldThrowNumberFormatException() {
			// when(userRepository.findById(Integer.valueOf("abc"))).thenReturn(Optional.empty());
			assertThrows(NumberFormatException.class,
					() -> formaUserDetailsService.loadUserByUsername("abc"));
		}
	}

}
