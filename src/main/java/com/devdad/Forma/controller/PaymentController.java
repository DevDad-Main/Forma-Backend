package com.devdad.Forma.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devdad.Forma.service.PaymentService;
import com.stripe.exception.StripeException;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/payments")
// @Slf4j
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/create-payment-intent")
    public Map<String, Object> createPayment(@RequestBody Map<String, Object> request) throws StripeException {
        return paymentService.createPayment(request);
    }
}
