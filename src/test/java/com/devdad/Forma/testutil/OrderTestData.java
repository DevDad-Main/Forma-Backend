package com.devdad.Forma.testutil;


import java.time.LocalDateTime;
import java.util.List;
import com.devdad.Forma.model.Order;
import com.devdad.Forma.model.OrderItem;
import com.devdad.Forma.model.OrderStatus;
import com.devdad.Forma.model.ShippingAddress;

public class OrderTestData {

    public static Order order1() {
        Order order = new Order();
				order.setId(1L);
        order.setUserId(1);
        order.setStatus(OrderStatus.PAID);
        order.setPaymentIntentId("pi_test_abc123");
        order.setChargeId("ch_test_abc123");
        order.setAmount(45000L);
        order.setCurrency("pln");
				order.setCreatedAt(LocalDateTime.now());
				order.setUpdatedAt(LocalDateTime.now());

        ShippingAddress addr = new ShippingAddress();
        addr.setStreet("ul. Marszałkowska 10");
        addr.setCity("Warsaw");
        addr.setState("Mazowieckie");
        addr.setZipCode("00-001");
        addr.setCountry("Poland");
        order.setShippingAddress(addr);

        OrderItem item1 = new OrderItem();
        item1.setProductId("prod_tshirt");
        item1.setProductName("Cotton T-Shirt");
        item1.setProductImage("tshirt.jpg");
        item1.setQuantity(2);
        item1.setPriceAtPurchase(49.99);

        OrderItem item2 = new OrderItem();
        item2.setProductId("prod_jeans");
        item2.setProductName("Slim Fit Jeans");
        item2.setProductImage("jeans.jpg");
        item2.setQuantity(1);
        item2.setPriceAtPurchase(199.99);

        order.setItems(List.of(item1, item2));
        order.setSubtotal(29997L);
        order.setShippingCost(15000L);
        order.setDiscount(0L);
        return order;
    }

    public static Order order2() {
        Order order = new Order();
				order.setId(2L);
        order.setUserId(2);
        order.setStatus(OrderStatus.PROCESSING);
        order.setPaymentIntentId("pi_test_def456");
        order.setChargeId("ch_test_def456");
        order.setAmount(12999L);
        order.setCurrency("pln");
				order.setCreatedAt(LocalDateTime.now());
				order.setUpdatedAt(LocalDateTime.now());

        ShippingAddress addr = new ShippingAddress();
        addr.setStreet("ul. Floriańska 5");
        addr.setCity("Krakow");
        addr.setState("Małopolskie");
        addr.setZipCode("30-001");
        addr.setCountry("Poland");
        order.setShippingAddress(addr);

        OrderItem item = new OrderItem();
        item.setProductId("prod_hat");
        item.setProductName("Baseball Cap");
        item.setProductImage("cap.jpg");
        item.setQuantity(3);
        item.setPriceAtPurchase(29.99);

        order.setItems(List.of(item));
        order.setSubtotal(8997L);
        order.setShippingCost(4000L);
        order.setDiscount(0L);
        return order;
    }
}
