package com.devdad.Forma.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

@Service
public class PaymentService {

    public Map<String, Object> createPayment(Map<String, Object> request) throws StripeException {
        Long amount = Long.valueOf(request.get("amount").toString());
        String currency = request.get("currency").toString();

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                // TODO: Add much more fields and logic.
                .setAmount(amount)
                .setCurrency(currency)
                .build();

        PaymentIntent intent = PaymentIntent.create(params);
        System.out.println("Payment intent created...");

        return Map.of("clientSecret", intent.getClientSecret());
    }

}
