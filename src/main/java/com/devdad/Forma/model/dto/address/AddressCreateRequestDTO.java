package com.devdad.Forma.model.dto.address;

import jakarta.validation.constraints.NotBlank;

public record AddressCreateRequestDTO(

		@NotBlank(message = "Street is required") String street,
		@NotBlank(message = "City is required") String city,
		String state,
		@NotBlank(message = "Country is required") String country,

		@NotBlank(message = "Zip code is required") String zipCode,
		Boolean isDefault) {
}
