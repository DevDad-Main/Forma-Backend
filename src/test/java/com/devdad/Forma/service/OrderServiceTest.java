package com.devdad.Forma.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

import com.devdad.Forma.model.User;
import com.devdad.Forma.model.UserPrinciple;
import com.devdad.Forma.model.dto.order.OrderPDFResponseDTO;
import com.devdad.Forma.model.dto.order.OrderResponseDTO;
import com.devdad.Forma.repository.OrderRepository;
import com.devdad.Forma.testutil.OrderTestData;

/**
 * OrderServiceTest
 */
@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

	@Mock
	private OrderRepository orderRepository;

	@Mock
	private Authentication authentication;

	@Mock
	private SecurityContext securityContext;

	@InjectMocks
	private OrderService orderService;

	private User testUser;

	@BeforeEach
	void setup() {
		testUser = User.builder().id(0).build();

		// NOTE: lenient() tells Mockito to not fail when a stub is declared but never
		// actually called during that test.
		lenient().when(securityContext.getAuthentication()).thenReturn(authentication);
		lenient().when(authentication.getPrincipal()).thenReturn(new UserPrinciple(testUser));
		SecurityContextHolder.setContext(securityContext);
	}

	@Nested
	@DisplayName("Get Orders Tests")
	class GetOrderTests {

		@Test
		void getAllOrders_shouldReturnListOfAllOrders() {
			when(orderRepository.findAllOrdersByUserId(testUser.getId()))
					.thenReturn(List.of(OrderTestData.order1(), OrderTestData.order2()));

			List<OrderResponseDTO> results = orderService.getAllOrders();

			assertNotNull(results);
			assertEquals(2, results.size());
			verify(orderRepository).findAllOrdersByUserId(testUser.getId());
		}

		@Test
		void getOrderById_shouldReturnAnOrderBySpecifiedID() {
			when(orderRepository.findByOrderNumber("1")).thenReturn(OrderTestData.order1());

			OrderPDFResponseDTO result = orderService.getOrderById(String.valueOf(OrderTestData.order1().getId()));

			assertNotNull(result);

			assertEquals(1, result.getId());
			verify(orderRepository).findByOrderNumber(String.valueOf(OrderTestData.order1().getId()));
		}

		@Test
		void getOrderById_shouldReturn404NotFoundExcpetionOnNoOrderFound() {
			assertThrows(ResponseStatusException.class, 
					() -> orderService.getOrderById("abc"));
		}
	}

}
