
package com.devdad.Forma.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.devdad.Forma.model.Order;
import com.devdad.Forma.model.User;
import com.devdad.Forma.model.UserPrinciple;
import com.devdad.Forma.repository.OrderRepository;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public List<Order> getAllOrders() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserPrinciple principle = (UserPrinciple) auth.getPrincipal();
        User user = principle.getUser();

        return orderRepository.findAllOrdersByUserId(user.getId());
    }
}
