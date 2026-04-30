
package com.devdad.Forma.model;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class Order {

	@Id
	@GeneratedValue
	private Long id;

	private String orderNumber; // Generated: "ORD-" + timestamp

	private int userId;

	@Enumerated(EnumType.STRING)
	private OrderStatus status;

	// Stripe Payment Info
	private String paymentIntentId;
	private String chargeId;
	private Long amount; // In cents
	private String currency; // pln

	// Items (OrderItem Entity or JSON column)
	@OneToMany(cascade = CascadeType.ALL)
	private List<OrderItem> items;

	// Shipping
	@Embedded
	private Address shippingAddress;

	private Long subtotal;
	private Long shipingCost;
	private Long discount;

	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
