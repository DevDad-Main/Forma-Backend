
package com.devdad.Forma.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.devdad.Forma.mapper.OrderMapper;
import com.devdad.Forma.mapper.OrderPDFMapper;
import com.devdad.Forma.model.Order;
import com.devdad.Forma.model.User;
import com.devdad.Forma.model.UserPrinciple;
import com.devdad.Forma.model.dto.order.OrderPDFResponseDTO;
import com.devdad.Forma.model.dto.order.OrderResponseDTO;
import com.devdad.Forma.repository.OrderRepository;

@Service
public class OrderService {

	@Autowired
	private OrderRepository orderRepository;

	public List<OrderResponseDTO> getAllOrders() {
		List<Order> orders = orderRepository.findAllOrdersByUserId(getUser().getId());
		List<OrderResponseDTO> orderResponseDTOs = orders
				.stream()
				.map(OrderMapper::toDTO)
				.toList();

		return orderResponseDTOs;
	}

	public OrderPDFResponseDTO getOrderById(String id) {
		Order order = orderRepository.findByOrderNumber(id);
		if(order == null){
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Order Found With ID: " + id);
		}
		return OrderPDFMapper.toDTO(order);
	}

	public User getUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserPrinciple principle = (UserPrinciple) auth.getPrincipal();
		return principle.getUser();
	}
}
