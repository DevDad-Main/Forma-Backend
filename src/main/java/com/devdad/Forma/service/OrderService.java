
package com.devdad.Forma.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.devdad.Forma.mapper.OrderMapper;
import com.devdad.Forma.model.Order;
import com.devdad.Forma.model.User;
import com.devdad.Forma.model.UserPrinciple;
import com.devdad.Forma.model.dto.order.OrderResponseDTO;
import com.devdad.Forma.repository.OrderRepository;

@Service
public class OrderService {

	@Autowired
	private OrderRepository orderRepository;

	public List<OrderResponseDTO> getAllOrders() {
		User user = getUser();
		List<Order> orders = orderRepository.findAllOrdersByUserId(user.getId());
		List<OrderResponseDTO> orderResponseDTOs = new ArrayList<>();

		for(Order order: orders){
			orderResponseDTOs.add(OrderMapper.toDTO(order));
		}

		return orderResponseDTOs;
	}

	public User getUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserPrinciple principle = (UserPrinciple) auth.getPrincipal();
		return principle.getUser();
	}
}
