package com.devdad.Forma.model.dto.payment;

import java.util.List;

import lombok.Data;

@Data
public class CreatePaymentIntentRequest {
	private Long amount; // in cents
	private String currency;
	private List<ProductItem> products;
	private Address shippingAddress;
	private Double shippingCost;
	private Double discount;

	// Getters and setters

	@Data
	public static class ProductItem {
		private String id;
		private String name;
		private Double price;
		private Integer quantity;
		// Getters and setters
	}

	@Data
	public static class Address {
		private String street;
		private String city;
		private String state;
		private String zipCode;
		private String country;
		// Getters and setters
	}
}
