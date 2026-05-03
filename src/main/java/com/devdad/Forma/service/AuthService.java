package com.devdad.Forma.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.devdad.Forma.config.SecurityConfig;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.devdad.Forma.exception.EmailAlreadyExistsException;
import com.devdad.Forma.model.User;
import com.devdad.Forma.model.UserPrinciple;
import com.devdad.Forma.model.dto.user.LoginResponse;
import com.devdad.Forma.model.dto.user.UserLoginResponse;
import com.devdad.Forma.model.dto.user.UserRegisterResponse;
import com.devdad.Forma.model.dto.user.UserResponse;
import com.devdad.Forma.model.dto.user.UserUpdateResponse;
import com.devdad.Forma.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class AuthService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public UserResponse getAuthenticatedUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserPrinciple principle = (UserPrinciple) auth.getPrincipal();
		User user = principle.getUser();

		System.out.println("User Principle: " + user);
		System.out.println("Auth Authorities: " + auth.getAuthorities());

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
		user.setPassword(passwordEncoder.encode(userRegisterDTO.password()));
		user = userRepository.save(user); // Need to save first to get the ID for JWT

		// Generate JWT token with the user's database ID as the subject.
		// This ID uniquely identifies this user in our DB.
		String token = jwtService.generateToken(String.valueOf(user.getId()));

		// Cookie is sent automatically with every request to our domain
		ResponseCookie cookie = ResponseCookie.from("jwt", token)
				.httpOnly(true) // JS can't read this cookie
				.secure(true) // set to TRUE in production
				.path("/") // Cookie is sent to all paths.
				.maxAge(24 * 60 * 60) // Expires in 24 hours.
				.sameSite("None") // Sent with same site requests
				.build();

		response.setHeader("Set-Cookie", cookie.toString());

		return user;
	}

	public LoginResponse validateAndAuthenticateUserForLogin(UserLoginResponse userLoginResponse,
			HttpServletResponse response) {
		try {
			System.out.println("=== LOGIN START ===");
			System.out.println("Email: " + userLoginResponse.email());

			Authentication authentication = authenticationManager
					.authenticate(
							new UsernamePasswordAuthenticationToken(userLoginResponse.email(), userLoginResponse.password()));

			System.out.println("Authentication result: " + authentication.isAuthenticated());

			if (!authentication.isAuthenticated()) {
				System.out.println("NOT AUTHENTICATED!");
				return null;
			}

			User user = userRepository.findByEmail(userLoginResponse.email()).orElseThrow();
			String token = jwtService.generateToken(String.valueOf(user.getId()));

			// Cookie is sent automatically with every request to our domain
			ResponseCookie cookie = ResponseCookie.from("jwt", token)
					.httpOnly(true) // JS can't read this cookie
					.secure(true) // set to TRUE in production
					.path("/") // Cookie is sent to all paths.
					.maxAge(24 * 60 * 60) // Expires in 24 hours.
					.sameSite("None") // Sent with same site requests
					.build();

			response.setHeader("Set-Cookie", cookie.toString());

			System.out.println("USER: " + user + " TOKEN: " + token + " COOKIE: " + cookie);
			System.out.println("=== LOGIN COMPLETE ===");

			return new LoginResponse(user, token);
		} catch (Exception e) {
			System.out.println("LOGIN ERROR: " + e.getMessage());
			e.printStackTrace();
			throw new RuntimeException("Login failed: " + e.getMessage());
		}
	}

	public UserUpdateResponse updateUserProfile(UserUpdateResponse userDetails) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserPrinciple principle = (UserPrinciple) auth.getPrincipal();
		User user = principle.getUser();

		if (user != null) {
			user.setFirstName(userDetails.firstName());
			user.setLastName(userDetails.lastName());
			userRepository.save(user);
		}
		return new UserUpdateResponse(user.getFirstName(), user.getLastName(), user.getEmail());
	}

	public void logout(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("=== LOGOUT START ===");
		
		// Log the cookies we received
		jakarta.servlet.http.Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (jakarta.servlet.http.Cookie c : cookies) {
				System.out.println("Received cookie: " + c.getName() + " = " + (c.getName().equals("jwt") ? "JWT_VALUE_HIDDEN" : c.getValue()));
			}
		}
		
		// Clear JWT cookie - use multiple approaches to ensure it's cleared
		// Approach 1: ResponseCookie with expired date
		ResponseCookie cookie = ResponseCookie.from("jwt", "")
				.httpOnly(true)
				.secure(true)
				.path("/")
				.maxAge(0)
				.sameSite("None")
				.build();
		response.setHeader("Set-Cookie", cookie.toString());
		
		// Approach 2: Standard Cookie class with maxAge=0
		jakarta.servlet.http.Cookie jwtCookie = new jakarta.servlet.http.Cookie("jwt", "");
		jwtCookie.setPath("/");
		jwtCookie.setMaxAge(0);
		jwtCookie.setHttpOnly(true);
		jwtCookie.setSecure(true);
		response.addCookie(jwtCookie);
		
		System.out.println("JWT cookie clear headers set");
		
		// Clear any session
		jakarta.servlet.http.HttpSession session = request.getSession(false);
		if (session != null) {
			System.out.println("Invalidating session: " + session.getId());
			session.invalidate();
		}
		
		// Clear JSESSIONID cookie as well
		jakarta.servlet.http.Cookie sessionCookie = new jakarta.servlet.http.Cookie("JSESSIONID", "");
		sessionCookie.setPath("/");
		sessionCookie.setMaxAge(0);
		sessionCookie.setHttpOnly(true);
		response.addCookie(sessionCookie);
		
		// Clear SecurityContext
		org.springframework.security.core.context.SecurityContextHolder.clearContext();
		System.out.println("SecurityContext cleared");
		System.out.println("=== LOGOUT END ===");
	}
}
