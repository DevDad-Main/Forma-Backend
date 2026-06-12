package com.devdad.Forma.mapper;

import static com.devdad.Forma.testutil.OrderTestData.order1;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.devdad.Forma.model.Order;
import com.devdad.Forma.model.dto.order.OrderPDFResponseDTO;

class OrderPDFMapperTest {

    @Nested
    @DisplayName("toDTO")
    class ToDTO {

        @Test
        void shouldMapOrderToOrderPDFResponseDTO() {
            Order order = order1();
            order.setOrderNumber("ORD@FMA-#123456789");

            OrderPDFResponseDTO dto = OrderPDFMapper.toDTO(order);

            assertAll(
                    () -> assertEquals(order.getId(), dto.getId()),
                    () -> assertEquals(order.getOrderNumber(), dto.getOrderNumber()),
                    () -> assertEquals(order.getUserId(), dto.getUserId()),
                    () -> assertNull(dto.getStatus()), // OrderStatus enum can't copy to String
                    () -> assertEquals(order.getPaymentIntentId(), dto.getPaymentIntentId()),
                    () -> assertEquals(order.getChargeId(), dto.getChargeId()),
                    () -> assertEquals(order.getAmount(), dto.getAmount()),
                    () -> assertEquals(order.getCurrency(), dto.getCurrency()),
                    () -> assertEquals(order.getItems(), dto.getItems()),
                    () -> assertNotNull(dto.getShippingAddress()),
                    () -> assertEquals(order.getShippingAddress().getStreet(),
                            dto.getShippingAddress().getStreet()),
                    () -> assertEquals(order.getSubtotal(), dto.getSubtotal()),
                    () -> assertEquals(order.getShippingCost(), dto.getShippingCost()),
                    () -> assertEquals(order.getDiscount(), dto.getDiscount()));
        }
    }
}
