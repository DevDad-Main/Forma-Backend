
package com.devdad.Forma.mapper;

import org.springframework.beans.BeanUtils;

import com.devdad.Forma.model.Order;
import com.devdad.Forma.model.dto.order.OrderPDFResponseDTO;

public class OrderPDFMapper {

	public static OrderPDFResponseDTO toDTO(Order order) {
		OrderPDFResponseDTO orderPDFResponseDTO = new OrderPDFResponseDTO();

		// Only because we are using the exact same properties so we know that this copy
		// will be correct, saves writing everything out manually
		BeanUtils.copyProperties(order, orderPDFResponseDTO);

		return orderPDFResponseDTO;
	}
}
