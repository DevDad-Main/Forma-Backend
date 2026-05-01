
package com.devdad.Forma.model;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "orders")
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
	@JsonIgnore // Don't serialize items in the order response
	private List<OrderItem> items;

	// Shipping address as embedded (no FK to addresses table)
	@Embedded
	private ShippingAddress shippingAddress;

	private Long subtotal;
	private Long shipingCost;
	private Long discount;

	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	@PrePersist
	protected void onCreate() {
		createdAt = LocalDateTime.now();
		updatedAt = LocalDateTime.now();
		orderNumber = "ORD@FMA-#" + System.currentTimeMillis();
	}
}
