package com.devdad.Forma.model;

import java.util.Collection;
import java.util.Collections;

import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * UserPrinciple - wraps our User entity for Spring Security.
 * 
 * Spring Security needs a "principal" (current user) that implements
 * UserDetails.
 * This class adapts our User entity to what Spring Security expects:
 * 
 * - getUsername() - Returns the user's unique identifier
 * - getPassword() - Returns the hashed password
 * - getAuthorities() - Returns the user's roles/permissions
 * - isAccountNonExpired(), etc. - Security checks (we return true)
 */
public class UserPrinciple implements UserDetails {

	private User user;

	public UserPrinciple(User user) {
		this.user = user;
	}
	
	public User getUser() {
		return user;
	}

	/*
	 * Returns the user's authorities (roles).
	 * We use the role stored in the Database.
	 * By default all users will be USER
	 * Example: "USERS" or "ADMIN"
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.singleton(new SimpleGrantedAuthority(user.getRole().toString()));
	}

	/*
	 * Returns the user's password (hashed).
	 * Used for traditional username/password login.
	 */
	@Override
	public String getPassword() {
		return user.getPassword();
	}

	/*
	 * Returns the user's unique identifier.
	 *
	 * IMPORTANT: This must match what the JWT contains as the subject.
	 * We store the database ID here so it can be compared with the JWT claim.
	 *
	 * The JWT is generated wth subject = user's database ID,
	 * so when we validate the token, we compare JWT subject with getUsername
	 */
	@Override
	public String getUsername() {
		return String.valueOf(user.getId());
	}
}
