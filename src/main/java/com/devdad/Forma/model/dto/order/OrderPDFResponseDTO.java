package com.devdad.Forma.model.dto.order;

import java.util.List;

import com.devdad.Forma.model.OrderItem;
import com.devdad.Forma.model.ShippingAddress;

import lombok.Data;

@Data
public class OrderPDFResponseDTO {
	private Long id;

	private String orderNumber; // Generated: "ORD-" + timestamp

	private int userId;

	private String status;

	private String paymentIntentId;
	private String chargeId;
	private Long amount; // In cents
	private String currency; // pln

	private List<OrderItem> items;

	private ShippingAddress shippingAddress;

	private Long subtotal;
	private Long shippingCost;
	private Long discount;

	private String createdAt;
	private String updatedAt;

}
