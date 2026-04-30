package com.devdad.Forma.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserUpdateResponse(
		@NotBlank String firstName,
		@NotBlank String lastName,
		@NotBlank @Email String email) {
}
