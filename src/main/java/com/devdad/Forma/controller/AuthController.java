package com.devdad.Forma.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devdad.Forma.model.User;
import com.devdad.Forma.model.UserPrinciple;
import com.devdad.Forma.model.dto.UserResponse;

@RestController
@RequestMapping("/api")
public class AuthController {

	@GetMapping("/auth/me")
	public ResponseEntity<UserResponse> getAuthUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserPrinciple principle = (UserPrinciple) auth.getPrincipal();
		User user = principle.getUser();

		UserResponse response = new UserResponse(
				user.getId(),
				user.getEmail(),
				user.getFirstName(),
				user.getLastName(),
				user.getRole(),
				user.getProfilePicture());

		return ResponseEntity.ok(response);
	}
}
