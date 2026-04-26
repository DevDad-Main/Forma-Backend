package com.devdad.Forma.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users") // Match PostgreSQL DB.
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String firstName;
	private String lastName;
	private String profilePicture;
	private String googleId;
	private String provider;

	@Column(unique = true, nullable = false)
	private String email;

	@Column(nullable = false)
	private String password;

}
