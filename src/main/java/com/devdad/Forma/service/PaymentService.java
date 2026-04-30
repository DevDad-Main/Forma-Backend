package com.devdad.Forma.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.devdad.Forma.config.StripeProperties;
import com.devdad.Forma.model.User;
import com.devdad.Forma.model.UserPrinciple;
import com.devdad.Forma.model.dto.payment.CreatePaymentIntentRequest;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import com.stripe.param.PaymentIntentCreateParams;

import jakarta.servlet.http.HttpServletRequest;
import tools.jackson.databind.ObjectMapper;

@Service
public class PaymentService {

    @Autowired
    private StripeProperties stripeProperties;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public Map<String, Object> createPaymentIntent(CreatePaymentIntentRequest request) throws StripeException {
        // Calulcate the real total from the products for security reasons as FE data
        // could be tampered with
        long total = request.getProducts().stream()
                .mapToLong(p -> (long) (p.getPrice() * p.getQuantity() * 100))
                .sum();

        // Add shipping and subtract discount(if any)
        if (request.getShippingCost() != null)
            total += (long) (request.getShippingCost() * 100);
        if (request.getDiscount() != null)
            total -= (long) (request.getDiscount() * 100);

        String userId = String.valueOf(getCurrentUser().getId());
        String productsJson = objectMapper.writeValueAsString(request.getProducts());
        String addressJson = objectMapper.writeValueAsString(request.getShippingAddress());
        // Create stripe paymentIntent
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(total)
                .setCurrency("pln") // add field to stripe properties so it's dynamic
                .putMetadata("userId", userId)
                .putMetadata("products", productsJson)
                .putMetadata("shippingAddress", addressJson)
                .build();

        PaymentIntent intent = PaymentIntent.create(params);

        return Map.of("clientSecret", intent.getClientSecret());
    }

    public String handleWebhookStripeEvent(HttpServletRequest request, String payload, String header) {
        Event event = null;

        try {
            event = Webhook.constructEvent(payload, header, stripeProperties.getStripeWebhookSecret());
        } catch (SignatureVerificationException e) {
            return "Signature verification failed";
        }

        switch (event.getType()) {
            case "payment_intent.succeeded":
                var intent = (com.stripe.model.PaymentIntent) event.getDataObjectDeserializer()
                        .getObject().orElse(null);
                if (intent != null) {
                    System.out.println("Payment Intent Succeeded: " + intent.getId() + " amount=" + intent.getAmount());

                    // TODO: Update db with our products and order(mark as paid)
                }
                break;

            case "charge.succeeded":
                var chargeIntent = (com.stripe.model.Charge) event.getDataObjectDeserializer()
                        .getObject().orElse(null);
                if (chargeIntent != null) {
                    System.out.println(
                            "Charge Succeeded: " + chargeIntent.getId() + " amount=" + chargeIntent.getAmount());

                    // TODO: Update db with our products and order(mark as paid)
                }
                break;
            case "payment_intent.payment_failed":
                var failedIntent = (com.stripe.model.PaymentIntent) event.getDataObjectDeserializer().getObject()
                        .orElse(null);

                if (failedIntent != null) {
                    System.out.println("Payment Intent Failed " + failedIntent.getId());

                    // TODO: Mark order as not paid and any other logic we need here
                }
                break;

            default:
                System.out.println("Unhandled event type: " + event.getType());
        }

        return "ok";
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserPrinciple principle = (UserPrinciple) auth.getPrincipal();
        User user = principle.getUser();

        return user = user != null ? user : null;
    }
}
