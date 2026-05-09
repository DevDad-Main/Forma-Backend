package com.devdad.Forma.model.dto.order;


public class OrderResponseDTO {

	private String orderNumber;
	private String createdAt;
	private String status;
	private String amount;

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}


	public String getAmount() {
		return amount;
	}

	public void setAmount(String subtotal) {
		this.amount = subtotal;
	}
}
