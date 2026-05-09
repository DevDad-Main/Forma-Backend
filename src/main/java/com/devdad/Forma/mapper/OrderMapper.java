
package com.devdad.Forma.mapper;

import com.devdad.Forma.model.Order;
import com.devdad.Forma.model.dto.order.OrderResponseDTO;

public class OrderMapper {

	public static OrderResponseDTO toDTO(Order order) {
		OrderResponseDTO orderDTO = new OrderResponseDTO();
		orderDTO.setOrderNumber(order.getOrderNumber());
		orderDTO.setCreatedAt(order.getCreatedAt().toString());
		orderDTO.setStatus(order.getStatus().toString());
		orderDTO.setAmount(order.getAmount().toString());
		return orderDTO;
	}
}
