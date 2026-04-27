package com.devdad.Forma.service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.devdad.Forma.config.JwtProperties;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@Service
public class JwtService {

    private final JwtProperties jwtProperties;
    private final Key signingKey;

    @Autowired
    public JwtService(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.signingKey = getKey();
    }

    public String generateToken(String userId) {
        Map<String, Object> claims = new HashMap<>();

        System.out.println("Claims: " + claims);

        return Jwts.builder()
                .claims(claims)
                .subject(userId)
                .issuer(jwtProperties.getIssuer())
                .issuedAt(new Date())
                .expiration(new Date(
                        System.currentTimeMillis() + jwtProperties.getExpiration()))
                .signWith(signingKey, SignatureAlgorithm.HS256).compact();

    }

    private Key getKey() {
        // Access fields directly instead of Lombok getters
        String secret = jwtProperties.getSecret();
        if (secret == null) {
            // Default secret for development
            secret = "ThisIsASecretKeyForDevelopmentOnly12345678901234567890";
        }
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

	public String extractUserId(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
		final Claims claims = extractAllClaims(token);
		return claimResolver.apply(claims);
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parser()
				.verifyWith((SecretKey) signingKey)
				.build().parseSignedClaims(token).getPayload();
	}

	public boolean validateToken(String token, UserDetails userDetails) {
		final String userId = extractUserId(token); // JWT Subject = user ID
		final String userIdFromDb = userDetails.getUsername(); // getUsername() returns ID
		return (userId.equals(userIdFromDb) && !isTokenExpired(token));
	}

	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	private Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}
}
