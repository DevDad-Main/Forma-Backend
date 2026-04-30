package com.devdad.Forma.model.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRegisterResponse(
		@NotBlank @Email String email,
		@NotBlank @Size(min = 6) String password,
		@NotBlank String firstName,
		@NotBlank String lastName

) {
}
