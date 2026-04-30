package com.devdad.Forma.model.dto.address;

public record AddressResponseDTO(
		Integer id,
		String street,
		String city,
		String state,
		String country,
		String zipCode,
		Boolean isDefault,
		Integer userId // Only return user ID, not full entity to stop lazy init errors
) {
}
