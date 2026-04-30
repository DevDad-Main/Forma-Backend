
package com.devdad.Forma.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class OrderItem {
	@Id
	@GeneratedValue
	private Long id;

	// Snapshot reference to the product
	private String productId;
	private String productName;
	private String productImage;

	private Integer quantity;

	// Ensure we capture the price at time of purchase
	private Double priceAtPurchase;
}
