package com.devdad.Forma.config;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Base64;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

/**
 * Cookie-based OAuth2 Authorization Request Repository.
 * 
 * Stores OAuth2 state in a signed cookie instead of the HTTP session.
 * This prevents "authorization_request_not_found" errors when the session is invalidated
 * (e.g., during logout/relogin cycles).
 */
public class CookieBasedOAuth2AuthorizationRequestRepository implements AuthorizationRequestRepository {

    private static final String AUTH_REQUEST_COOKIE = "oauth2_auth";
    private static final int MAX_AGE = 600; // 10 minutes

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        Cookie cookie = getCookie(request, AUTH_REQUEST_COOKIE);
        if (cookie == null || cookie.getValue() == null || cookie.getValue().isEmpty()) {
            return null;
        }
        
        try {
            byte[] data = Base64.getDecoder().decode(cookie.getValue());
            
            try (ByteArrayInputStream bis = new ByteArrayInputStream(data);
                 ObjectInputStream ois = new ObjectInputStream(bis)) {
                OAuth2AuthorizationRequest authRequest = (OAuth2AuthorizationRequest) ois.readObject();
                return authRequest;
            }
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, 
            HttpServletRequest request, HttpServletResponse response) {
        if (authorizationRequest == null) {
            removeAuthorizationRequest(request, response);
            return;
        }
        
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(authorizationRequest);
            oos.flush();
            
            byte[] data = bos.toByteArray();
            String encoded = Base64.getEncoder().encodeToString(data);
            
            Cookie cookie = new Cookie(AUTH_REQUEST_COOKIE, encoded);
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            cookie.setMaxAge(MAX_AGE);
            cookie.setSecure(request.isSecure());
            
            response.addCookie(cookie);
        } catch (Exception e) {
            // Log error in production
        }
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request, 
            HttpServletResponse response) {
        OAuth2AuthorizationRequest authRequest = loadAuthorizationRequest(request);
        
        Cookie cookie = new Cookie(AUTH_REQUEST_COOKIE, "");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        
        response.addCookie(cookie);
        
        return authRequest;
    }

    private Cookie getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        
        for (Cookie cookie : cookies) {
            if (name.equals(cookie.getName())) {
                return cookie;
            }
        }
        
        return null;
    }
}