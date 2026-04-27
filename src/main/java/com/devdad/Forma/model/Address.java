package com.devdad.Forma.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "addresses")
public class Address {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(nullable = false)
	private String street;

	@Column(nullable = false)
	private String city;

	@Column(length = 10, nullable = false)
	private String state;

	@Column(name = "zip_code", length = 20, nullable = false)
	private String zipCode;

	@Column(nullable = false)
	private String country;

	@Column(name = "is_default")
	private boolean isDefault;

	@ManyToOne(targetEntity = User.class)
	private User user;
}
