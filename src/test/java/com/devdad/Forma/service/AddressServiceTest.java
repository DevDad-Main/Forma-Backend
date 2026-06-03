package com.devdad.Forma.service;

import com.devdad.Forma.model.Address;
import com.devdad.Forma.model.User;
import com.devdad.Forma.model.UserPrinciple;
import com.devdad.Forma.model.dto.address.AddressCreateRequestDTO;
import com.devdad.Forma.model.dto.address.AddressResponseDTO;
import com.devdad.Forma.repository.AddressRepository;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddressServiceTest {

	@Mock
	private AddressRepository addressRepository;

	@Mock
	private Authentication authentication;

	@Mock
	private SecurityContext securityContext;

	@InjectMocks
	private AddressService addressService;

	private User testUser;

	@BeforeEach
	void setUp() {
		testUser = new User();
		testUser.setId(1);

		when(securityContext.getAuthentication()).thenReturn(authentication);
		when(authentication.getPrincipal()).thenReturn(new UserPrinciple(testUser));
		SecurityContextHolder.setContext(securityContext);
	}

	@Nested
	@DisplayName("Get Address Tests")
	class GetAddressTests {

		@Test
		void getCurrentUserAddresses_returnsAddressesForAuthenticatedUser() {
			Address a1 = new Address();
			a1.setId(1);

			Address a2 = new Address();
			a2.setId(2);

			when(addressRepository.findAllByUserId("1")).thenReturn(List.of(a1, a2));

			List<Address> result = addressService.getCurrentUserAddresses();

			assertEquals(2, result.size());
		}
	}

	@Nested
	@DisplayName("Create Address Tests")
	class CreateAddressTests {

		@Test
		void createAddress_throws422WhenMaxExceeded() {
			when(addressRepository.findAllByUserId("1")).thenReturn(List.of(new Address(), new Address(), new Address()));

			AddressCreateRequestDTO dto = new AddressCreateRequestDTO("Street", "City", "State", "US", "12345", false);

			ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> addressService.createAddress(dto));
			assertEquals(422, ex.getStatusCode().value());
			verify(addressRepository, never()).save(any());
		}

		@Test
		void createAddress_savesAndReturnsDTO() {
			when(addressRepository.findAllByUserId("1")).thenReturn(List.of());

			Address saved = new Address();
			saved.setId(1);
			saved.setStreet("123 Main St");
			saved.setCity("Portland");
			saved.setState("OR");
			saved.setCountry("US");
			saved.setZipCode("97201");
			saved.setDefault(true);
			saved.setUser(testUser);
			when(addressRepository.save(any())).thenReturn(saved);

			AddressCreateRequestDTO dto = new AddressCreateRequestDTO("123 Main St", "Portland", "OR", "US", "97201", true);

			AddressResponseDTO result = addressService.createAddress(dto);

			assertEquals("123 Main St", result.street());
			assertEquals("Portland", result.city());
			assertTrue(result.isDefault());
		}
	}

	@Nested
	@DisplayName("Update Address Tests")
	class UpdateAddressTests {

		@Test
		void updateAddress_throwsWhenNotFound() {
			when(addressRepository.findAllByUserId("1")).thenReturn(List.of());

			Address input = new Address();
			input.setId(99);

			assertThrows(IllegalArgumentException.class, () -> addressService.updateAddress(input));
		}
	}

	@Nested
	@DisplayName("Delete Address Tests")
	class DeleteAddressTests {

		@Test
		void deleteAddress_returnsTrueWhenFound() {
			Address addr = new Address();
			addr.setId(5);
			when(addressRepository.findAllByUserId("1")).thenReturn(List.of(addr));

			assertTrue(addressService.deleteAddress("5"));
			verify(addressRepository).delete(addr);
		}

		@Test
		void deleteAddress_returnsFalseWhenNotFound() {
			when(addressRepository.findAllByUserId("1")).thenReturn(List.of());

			assertFalse(addressService.deleteAddress("999"));
			verify(addressRepository, never()).delete(any());
		}
	}
}
