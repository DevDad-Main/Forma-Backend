package com.devdad.Forma.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import com.devdad.Forma.model.Order;
import com.devdad.Forma.model.User;
import com.devdad.Forma.model.UserPrinciple;
import com.devdad.Forma.service.OrderService;
import com.devdad.Forma.testutil.OrderTestData;
import com.devdad.Forma.testutil.UserTestData;

@DataJpaTest
@Import(OrderService.class)
public class OrderServiceIntegrationTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private OrderService orderService;

	private User savedUser;

	@BeforeEach
	void setUpAuth() {
		User user = UserTestData.user();
		savedUser = entityManager.persist(user);

		var principle = new UserPrinciple(savedUser);
		var auth = new TestingAuthenticationToken(
				principle,
				null,
				principle.getAuthorities());

		SecurityContextHolder.getContext().setAuthentication(auth);
	}

	@Test
	void getAllOrders_shouldReturnListOfOrdersForCurrentUser() {
		Order order1 = OrderTestData.order1();
		order1.setId(null);
		order1.setUserId(savedUser.getId());
		entityManager.persist(order1);

		Order order2 = OrderTestData.order2();
		order2.setId(null);
		order2.setUserId(savedUser.getId());
		entityManager.persist(order2);

		var results = orderService.getAllOrders();

		assertNotNull(results);
		assertEquals(2, results.size());
	}

	@Test
	void getOrderById_shouldReturnOrderByOrderNumber() {
		Order order = OrderTestData.order1();
		order.setId(null);
		order.setUserId(savedUser.getId());
		entityManager.persist(order);

		var result = orderService.getOrderById(order.getOrderNumber());

		assertNotNull(result);
		assertEquals(order.getId(), result.getId());
	}
}
