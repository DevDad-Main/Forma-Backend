package com.devdad.Forma.model.dto;

import com.devdad.Forma.model.User;

public record LoginResponse(
		User user,
		String token) {
}
