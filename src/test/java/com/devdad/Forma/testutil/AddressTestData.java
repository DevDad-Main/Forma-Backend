package com.devdad.Forma.testutil;

import com.devdad.Forma.model.Address;
import com.devdad.Forma.model.dto.address.AddressCreateRequestDTO;
import com.devdad.Forma.model.dto.address.AddressResponseDTO;

public class AddressTestData {

	public static Address address() {
		Address addr = new Address();
		addr.setId(1);
		addr.setStreet("123 Main St");
		addr.setCity("Portland");
		addr.setState("OR");
		addr.setZipCode("97201");
		addr.setCountry("US");
		addr.setDefault(true);
		return addr;
	}

	public static AddressCreateRequestDTO addressCreateRequestDTO() {
		return new AddressCreateRequestDTO("123 Main St", "Portland", "OR", "US", "97201", true);
	}

	public static AddressResponseDTO addressResponseDTO() {
		return new AddressResponseDTO(1, "123 Main St", "Portland", "OR", "US", "97201", true, 1);
	}
}
