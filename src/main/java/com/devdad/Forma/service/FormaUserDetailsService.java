package com.devdad.Forma.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.devdad.Forma.model.User;
import com.devdad.Forma.model.UserPrinciple;
import com.devdad.Forma.repository.UserRepository;

/*
 * FormaUserDetailsService - Loads user data for Spring Security.
 *
 * This service implements Spring's UserDetailsService interface.
 * Spring Security calls this when it needs to load a user's information:
 *
 * WHEN IT'S CALLED:
 * 1. During JWT validation (JwtFilter)
 * 2. During traditional username/password login
 *
 * HOW IT WORKS:
 * 1. Receives a user ID (as a String)
 * 2. Looks up the user in the database
 * 3. Wraps the User entity in a UserPrinciple
 * 4. Returns the UserPrinciple (which implements UserDetails)
 *
 * The UserPrinciple is what Spring Security uses to check:
 * - Is the user valid?
 * - What roles/authorities does the user have?
 * - What is the user's identifier?
 */
@Service
public class FormaUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	/**
	 * Loads a user by their database ID.
	 * 
	 * @param identifier - The user's database ID as a String (e.g., "1", "2")
	 * @return UserDetails - A UserPrinciple wrapping the User entity
	 * @throws UsernameNotFoundException - If user with given ID doesn't exist
	 */
	@Override
	public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {

		Optional<User> optionalUser = null;

		// Convert String ID to Integer and look up in the database.
		if (identifier.contains("@")) {
			optionalUser = userRepository.findByEmail(identifier);
		} else {
			optionalUser = userRepository.findById(Integer.valueOf(identifier));
		}

		// If user dosen't exist, throws exception
		if (optionalUser.isEmpty()) {
			throw new UsernameNotFoundException("User Not Found: " + identifier);
		}

		// Get the user from Optional wrap in UserPrinciple
		User user = optionalUser.get();
		return new UserPrinciple(user);

	}

}
