package com.devdad.Forma.model.dto.user;

public record UserResponse(
		int id,
		String email,
		String firstName,
		String lastName,
		String role,
		String profilePictureUrl) {
}
