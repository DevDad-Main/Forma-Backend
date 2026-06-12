package com.devdad.Forma.mapper;

import static com.devdad.Forma.testutil.OrderTestData.order1;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.devdad.Forma.model.Order;
import com.devdad.Forma.model.dto.order.OrderResponseDTO;

class OrderMapperTest {

    @Nested
    @DisplayName("toDTO")
    class ToDTO {

        @Test
        void shouldMapOrderToOrderResponseDTO() {
            Order order = order1();
            order.setOrderNumber("ORD@FMA-#123456789");

            OrderResponseDTO dto = OrderMapper.toDTO(order);

            assertAll(
                    () -> assertEquals("ORD@FMA-#123456789", dto.getOrderNumber()),
                    () -> assertEquals(order.getCreatedAt().toString(), dto.getCreatedAt()),
                    () -> assertEquals(order.getStatus().toString(), dto.getStatus()),
                    () -> assertEquals(order.getAmount().toString(), dto.getAmount()));
        }
    }
}
