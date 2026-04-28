package com.devdad.Forma.config;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.web.server.Cookie;
import jakarta.servlet.http.Cookie;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.devdad.Forma.service.JwtService;
import com.devdad.Forma.model.User;
import com.devdad.Forma.repository.UserRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Handles what happens after successful OAuth2 (Google) login.
 * 
 * OAuth2 Flow:
 * 1. User clicks "Login with Google"
 * 2. User is redirected to Google, enters credentials
 * 3. Google redirects back with a code
 * 4. THIS handler runs
 * 5. We create/update user in DB
 * 6. Generate JWT token
 * 7. Set JWT as HTTP-only cookie
 * 8. Clear OAuth2 session to prevent loops
 * 9. Return JSON response (frontend handles redirect)
 */
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private final JwtService jwtService;

	@Autowired
	private UserRepository UserRepository;

	@Autowired
	private ApplicationContext ctx;

	public OAuth2SuccessHandler(JwtService jwtService) {
		this.jwtService = jwtService;
	}

	/**
	 * This method runs when OAuth2 login is successful.
	 * 
	 * @param request        - The HTTP request
	 * @param response       - The HTTP response (we write the JWT cookie here)
	 * @param authentication - Contains the OAuth2 user's info from Google
	 */
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		// Extract the OAuth2 user from googles response
		// authentication.getPrincipal() contains all user info like email, name,
		// picture, ids etc..

		OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();

		System.out.println("OAuth Object: " + oauthUser);

		// Extract user details from Googles OAuth2 response
		String email = oauthUser.getAttribute("email"); // User's email
		String firstName = oauthUser.getAttribute("given_name"); // First Name
		String lastName = oauthUser.getAttribute("family_name"); // Last Name
		String picture = oauthUser.getAttribute("picture"); // Profile Pic URL
		String googleId = oauthUser.getAttribute("sub"); // Googles Unique ID for this user
		// Find existing user by Google ID, or we create a new one.
		// This ensures each google account maps to one user in our DB.
		User user = UserRepository.findByGoogleId(googleId)
				.orElseGet(() -> {
					// New User to save to the db

					User newUser = new User();
					newUser.setEmail(email);
					newUser.setFirstName(firstName);
					newUser.setLastName(lastName);
					newUser.setGoogleId(googleId);
					newUser.setProvider("GOOGLE"); // Mark as Googles OAuth user.
					newUser.setProfilePicture(picture);

					// Generate random password (OAuth users don't need password)
					SecurityConfig securityConfig = ctx.getBean(SecurityConfig.class);
					newUser.setPassword(securityConfig.passwordEncoder().encode(UUID.randomUUID().toString()));

					System.out.println("New User Created: " + newUser);
					return UserRepository.save(newUser);
				});

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

		// CRITICAL: CLear the OAuth2 session state
		// Without this, Spring might re-trigger the OAuth2 flow on next request(could
		// be the issue i was facing in my other test app)
		SecurityContextHolder.clearContext();

		// Also invalidate the session to prevent OAuth2 state reuse
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();

			// Also clear the JSESSIONID cookie that lingers around
			Cookie sessionCookie = new Cookie("JSESSIONID", "");
			sessionCookie.setPath("/");
			sessionCookie.setMaxAge(0);
			sessionCookie.setHttpOnly(true);
			response.addCookie(sessionCookie);
		}

		// Clear the oauth2_auth cookie to prevent reuse
		jakarta.servlet.http.Cookie authCookie = new jakarta.servlet.http.Cookie("oauth2_auth", "");
		authCookie.setPath("/");
		authCookie.setMaxAge(0);
		authCookie.setHttpOnly(true);
		response.addCookie(authCookie);

		// Redirect to this url as the frontend listens to this URI and then navigates
		// the users to their profile.
		response.sendRedirect("http://localhost:5173/login/oauth2/code/google");

		// // Return JSON instead of redirecting
		// // This lets the frontend decide where to go next
		// // Frontend should read the JWT cookie and handle navigation
		// response.setContentType("application/json");
		// response.setStatus(200);
		// // response.getWriter().write("{\"success\": true, \"redirect\":
		// // \"http://localhost:8080/about\"}");
		// response.getWriter().write("{\"success\": true, \"redirect\":
		// \"http://localhost:5173\"}");
		// response.getWriter().flush();
	}

}
