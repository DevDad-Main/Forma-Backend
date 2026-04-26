package com.devdad.Forma.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Security Configuration for the application.
 * 
 * This class configures:
 * 1. Which endpoints are public vs protected
 * 2. How users authenticate (OAuth2 Google login)
 * 3. How JWT tokens are validated on each request
 * 4. Session management policy
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private JwtFilter jwtFilter;

	@Autowired
	private OAuth2SuccessHandler oAuth2SuccessHandler;

	/*
	 * Creates a Bcrypt password encoder for hashing passwords.
	 * The number (12) is the "strength"(Salts) - higher= more secure but slower
	 */
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(12);
	}

	/**
	 * Configures the authentication provider that validates user credentials.
	 * This connects Spring Security to our UserRepository via
	 * DaoAuthenticationProvider.
	 */
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
		provider.setPasswordEncoder(passwordEncoder());
		return provider;
	}

	/**
	 * Main security filter chain configuration.
	 * This determines how requests are authenticated and authorized.
	 */
	@Bean
	public SecurityFilterChain securityFilterChainLambda(HttpSecurity http) throws Exception {

		// Disable CSRF (Cross-Site Request Forgery protection)
		// We disable this because we're using JWT tokens which are immune to CSRF
		// CSRF requires stateful cookies, but we use stateless tokens
		http.csrf(csrf -> csrf.disable());

		// Enable CORS
		http.cors(cors -> cors.configurationSource(corsConfigurationSource()));

		// Step 2: Define which URLs are public (no login required)
		// These paths allow anyone to access them:
		// - /register, /login: Registration and login pages
		// - /oauth2/**: Google's OAuth2 endpoints (login flow)
		// - /error: Error page
		http.authorizeHttpRequests(request -> request
				.requestMatchers("/register", "/login", "/oauth2/**", "/error")
				.permitAll()
				// All other URLs require authentication
				.anyRequest().authenticated());

		// Configure Google OAuth2 login
		// When a user visits /oauth2/authorization/google, they get redirected to
		// Google
		// After Google login, OAuth2SuccessHandler runs
		http.oauth2Login(oauth -> oauth
				.successHandler(oAuth2SuccessHandler)
				.failureUrl("/login?error=true"));

		// Session management
		// IF_REQUIRED: Only create a session if OAuth2 flow needs it
		// We don't use STATELESS because OAuth2 requires a session during the login
		// flow
		http.sessionManagement(session -> session
				.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));

		// Add our JWT filter BEFORE OAuth2 redirect filter
		// This is critical! It checks the JWT cookie BEFORE Spring can redirect to
		// Google OAuth
		// Filter order matters - this ensures valid JWTs are recognized immediately
		http.addFilterBefore(
				jwtFilter,
				org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter.class);

		return http.build();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) {
		return config.getAuthenticationManager();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();
		config.addAllowedOrigin("http://localhost:5173");
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");
		config.setAllowCredentials(true);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return source;
	}
}
