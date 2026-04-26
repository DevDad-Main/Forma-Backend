package com.devdad.Forma.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import com.devdad.Forma.model.User;

@Repository
public interface UserRepository extends JpaRepositoryImplementation<User, Integer> {

	/**
	 * With the help of JPA's DSL we can fetch the user by google
	 * id
	 * 
	 * @param googleId
	 * @return User Object || null
	 */
	Optional<User> findByGoogleId(String googleId);

}
