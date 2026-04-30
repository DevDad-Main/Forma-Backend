package com.devdad.Forma.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.stripe.Stripe;

import jakarta.annotation.PostConstruct;
import lombok.Data;

@Data
@Configuration
public class StripeProperties {

    @Value("${stripe.api.secret-key}")
    private String stripeSecretKey;

    @Value("${stripe.api.publishable-key}")
    private String stripePublishableKey;

    @Value("${stripe.api.webhook-secret}")
    private String stripeWebhookSecret;

    @PostConstruct
    public void initializeKey() {
        Stripe.apiKey = stripeSecretKey;
    }
}
