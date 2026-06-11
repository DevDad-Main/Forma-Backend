package com.devdad.Forma.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import com.devdad.Forma.model.Address;
import com.devdad.Forma.model.User;
import com.devdad.Forma.model.UserPrinciple;
import com.devdad.Forma.service.AddressService;
import com.devdad.Forma.testutil.AddressTestData;
import com.devdad.Forma.testutil.UserTestData;

/**
 * AddressServiceIntegrationTest
 */

@DataJpaTest
@Import(AddressService.class)
public class AddressServiceIntegrationTest {

	private static final int MAX_ADDRESSES = 3;

	@Autowired
	private AddressService addressService;

	@Autowired
	private TestEntityManager entityManager;

	private User savedUser;

	@BeforeEach
	void setUpAuth() {
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
	void getCurrentUserAddresses_shouldReturnAUsersCurrentAddresses() {
		Address address = AddressTestData.address();
		address.setUser(savedUser);

		entityManager.persist(address);

		var result = addressService.getCurrentUserAddresses();

		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals("123 Main St", result.get(0).getStreet());
	}

	@Test
	void createAddress_shouldSuccessfullyCreateANewAddress() {
		var dto = AddressTestData.addressCreateRequestDTO1();
		var result = addressService.createAddress(dto);

		assertNotNull(result);

		assertEquals("123 Main St", result.street());
		assertEquals("Portland", result.city());
		assertEquals("OR", result.state());
		assertEquals("US", result.country());
		assertEquals("97201", result.zipCode());
		assertEquals(true, result.isDefault());
	}

	@Test
	void updateAddress_shouldSuccessfullyUpdateAnExistingAddress() {
		var dto = AddressTestData.addressCreateRequestDTO1();
		var created = addressService.createAddress(dto);
		int savedId = created.id();

		var updatedDTO = AddressTestData.addressCreateRequestDTO2();
		var result = addressService.updateAddress(savedId, updatedDTO);

		assertEquals(savedId, result.id());
		assertEquals("321 Second St", result.street());
		assertEquals("97202", result.zipCode());
	}

	@Test
	void deleteAddress_shouldSuccessfullyDeleteAnAddress() {
		var newAddress = AddressTestData.address();
		newAddress.setUser(savedUser);
		entityManager.persist(newAddress);

		boolean isDeleted = addressService.deleteAddress(String.valueOf(newAddress.getId()));

		assertTrue(isDeleted);
		assertNull(entityManager.find(Address.class, newAddress.getId()));
	}
}
