package com.devdad.Forma.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devdad.Forma.config.StripeProperties;
import com.devdad.Forma.model.dto.payment.CreatePaymentIntentRequest;
import com.devdad.Forma.service.PaymentService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
// @Slf4j
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/payments/create-payment-intent")
    public Map<String, Object> createPaymentIntent(@RequestBody CreatePaymentIntentRequest request)
            throws StripeException {
        return paymentService.createPaymentIntent(request);
    }

    @PostMapping("/webhooks/stripe")
    public String handleStripeWebhookEvent(
            HttpServletRequest request,
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String header) {

        return paymentService.handleWebhookStripeEvent(request, payload, header);
    }
}
