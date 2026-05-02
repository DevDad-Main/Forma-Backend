package com.devdad.Forma.config;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import com.devdad.Forma.service.FormaUserDetailsService;
import com.devdad.Forma.service.JwtService;
import com.nimbusds.jwt.proc.ExpiredJWTException;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.lang.Arrays;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

	@Autowired
	private JwtService jwtService;

	@Autowired
	private ApplicationContext ctx;

	private static final AntPathMatcher pathMatcher = new AntPathMatcher();

	private static final List<String> PUBLIC_PATHS_LIST = List.of(
			"/api/auth/register",
			"/api/auth/login",
			"/api/auth/logout",
			"/api/products/**",
			"/oauth2/**",
			"/login/oauth2/**",
			"/error",
			"/api/webhooks/**");

	/**
	 * This method runs for every incoming HTTP request.
	 * 
	 * @param request     - The HTTP request (contains headers, cookies)
	 * @param response    - The HTTP response
	 * @param filterChain - Passes request to next filter/endpoint
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			// Skip JWT processing for options requests (CORS preflight)
			if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
				filterChain.doFilter(request, response);
				return;
			}

			String requestURI = request.getRequestURI();
			System.out.println("JwtFilter: " + requestURI);

			// Skip JWT processing for public paths entirely
			if (isPublicPath(requestURI)) {
				filterChain.doFilter(request, response);
				return;
			}

			String authHeader = request.getHeader("Authorization");
			String token = null;

			// Try to get JWT from Authorization header
			// Format: "Bearer ejghskskjdak"
			if (authHeader != null && authHeader.startsWith("Bearer ")) {
				token = authHeader.substring(7); // Remove "Bearer " prefix
			}

			// If no header token, check cookies
			if (token == null && request.getCookies() != null) {
				for (Cookie cookie : request.getCookies()) {
					System.out.println("Cookie: " + cookie.getName() + " = " +
							cookie.getValue().substring(0, Math.min(20, cookie.getValue().length())));
					if ("jwt".equals(cookie.getName())) {
						token = cookie.getValue();
						System.out.println("JWT cookie found!");
						break;
					}
				}
			}

			// If we have a token, then we validate it.
			if (token != null) {
				String userId = jwtService.extractUserId(token);
				System.out.println("Extracted UserID: " + userId);

				// Only authenticate if:
				// - We got a userid from the token
				// - User isn't already authenticated (prevents re-authentication (issue from
				// before))
				if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
					// Load the user from the DB using their id
					UserDetails userDetails = ctx
							.getBean(FormaUserDetailsService.class)
							.loadUserByUsername(userId);

					// Validate Token:
					// - Token subject (userId) matches the loaded user
					// - Token hsn't expired
					if (jwtService.validateToken(token, userDetails)) {
						System.out.println("JWT Valid!");

						// Create authentication token with users authorities (roles)
						UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null,
								userDetails.getAuthorities());

						// Attach request details to the auth token
						authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

						// CRITICAL: Set authentication in SecurityContext
						// This tells Spring "this request is authenticated as this user"
						SecurityContextHolder.getContext().setAuthentication(authToken);
					} else {
						System.out.println("JWT Invalid!");
					}
				}
			}

			filterChain.doFilter(request, response);

		} catch (ExpiredJwtException e) {
			// First we clear the expired cookie
			clearJwtCookie(response);
			// clear session based auth
			SecurityContextHolder.clearContext();

			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.setContentType("application/json; charset=UTF-8");
			response.getWriter().write("{\"error\":\"Token expired\"}");
			response.getWriter().flush();
			return;
		} catch (JwtException e) {
			// First we clear the expired cookie
			clearJwtCookie(response);
			// clear session based auth
			SecurityContextHolder.clearContext();

			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.setContentType("application/json; charset=UTF-8");
			response.getWriter().write("{\"error\":\"Invalid token\"}");
			response.getWriter().flush();
			return;
		}
	}

	/**
	 * Takes an incoming Request URI and checks it against the allowed public paths.
	 * 
	 * @param requestURI
	 * @return true or false if the request is in the valid paths list.
	 */
	private boolean isPublicPath(String requestURI) {
		return PUBLIC_PATHS_LIST.stream().anyMatch(pattern -> pathMatcher.match(pattern, requestURI));
	}

	/**
	 * Wrapper method to clear the "jwt" cookie.
	 * 
	 * @param response
	 */
	private void clearJwtCookie(HttpServletResponse response) {
		Cookie cookie = new Cookie("jwt", "");

		cookie.setHttpOnly(true);
		cookie.setMaxAge(0);
		cookie.setPath("/");
		response.addCookie(cookie);
	}

}
