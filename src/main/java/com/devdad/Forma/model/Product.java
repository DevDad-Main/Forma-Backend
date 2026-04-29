package com.devdad.Forma.model;

import java.util.List;

import jakarta.annotation.Nullable;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "products")
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private Double price;

	@Column(nullable = true)
	private Double originalPrice; // optional, promotions, discounts etc
																//
	@Column(nullable = false)
	private String image;

	@Column(nullable = true)
	private String hoverImage; // Optional

	@Column(nullable = true)
	private String dimensions; // optional

	@ElementCollection
	@CollectionTable(name = "product_tags", joinColumns = @JoinColumn(name = "product_id"))
	@Column(nullable = true, name = "tag")
	private List<String> tags; // Optional

	@Column(nullable = false)
	private Boolean inStock; // Required

	@Column(nullable = true)
	private Boolean isNew; // optional

	@Column(nullable = true)
	private Boolean isBestSeller; // optional

	@Column(nullable = false)
	private String category; // Required

	@Column(nullable = true)
	private String material; // Optional

	@Column(nullable = true)
	private String color; // Optional

	@Column(nullable = false)
	private String description;
}
