package com.devdad.Forma.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.devdad.Forma.config.SecurityConfig;
import com.devdad.Forma.exception.EmailAlreadyExistsException;
import com.devdad.Forma.model.User;
import com.devdad.Forma.model.UserPrinciple;
import com.devdad.Forma.model.dto.UserRegisterResponse;
import com.devdad.Forma.model.dto.UserResponse;
import com.devdad.Forma.repository.UserRepository;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
public class AuthService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private SecurityConfig securityConfig;

	@Autowired
	private JwtService jwtService;

	public UserResponse getAuthenticatedUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserPrinciple principle = (UserPrinciple) auth.getPrincipal();
		User user = principle.getUser();

		System.out.println("User Principle: " + user);

		UserResponse response = new UserResponse(
				user.getId(),
				user.getEmail(),
				user.getFirstName(),
				user.getLastName(),
				user.getRole(),
				user.getProfilePicture());

		return response;
	}

	public User registerUser(UserRegisterResponse userRegisterDTO, HttpServletResponse response) {

		if (userRepository.findByEmail(userRegisterDTO.email()).isPresent()) {
			throw new EmailAlreadyExistsException(userRegisterDTO.email());
		}

		User user = new User();
		user.setEmail(userRegisterDTO.email());
		user.setFirstName(userRegisterDTO.firstName());
		user.setLastName(userRegisterDTO.lastName());
		user.setPassword(securityConfig.passwordEncoder()
				.encode(userRegisterDTO.password()));
		user = userRepository.save(user); // Need to save first to get the ID for JWT

		// Generate JWT token with the user's database ID as the subject.
		// This ID uniquely identifies this user in our DB.
		String token = jwtService.generateToken(String.valueOf(user.getId()));

		// Cookie is sent automatically with every request to our domain
		ResponseCookie cookie = ResponseCookie.from("jwt", token)
				.httpOnly(true) // JS can't read this cookie
				.secure(false) // set to TRUE in production
				.path("/") // Cookie is sent to all paths.
				.maxAge(24 * 60 * 60) // Expires in 24 hours.
				.sameSite("Lax") // Sent with same site requests
				.build();

		response.setHeader("Set-Cookie", cookie.toString());

		return user;
	}
}
