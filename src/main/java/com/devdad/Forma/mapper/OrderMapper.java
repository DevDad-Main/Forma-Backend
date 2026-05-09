
package com.devdad.Forma.mapper;


import com.devdad.Forma.model.Order;
import com.devdad.Forma.model.dto.order.OrderResponseDTO;

public class OrderMapper {

	
	public static OrderResponseDTO toDTO(Order order){
		OrderResponseDTO orderDTO = new OrderResponseDTO();
		orderDTO.setId(order.getId().toString());
		orderDTO.setCreatedAt(order.getCreatedAt().toString());
		orderDTO.setStatus(order.getStatus().toString());
		orderDTO.setSubtotal(order.getSubtotal().toString());
		orderDTO.setShippingCost(order.getShippingCost().toString());
		orderDTO.setShippingAddress(order.getShippingAddress());
		return orderDTO;
	}
}
