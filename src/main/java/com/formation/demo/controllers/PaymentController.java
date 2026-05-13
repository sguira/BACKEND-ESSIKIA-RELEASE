package com.formation.demo.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.formation.demo.dto.IntentPaymentRequestDTO;
import com.formation.demo.services.PayementService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PayementService payementService;

    @PostMapping("/create-payement-intent")
    public ResponseEntity<?> createPaymentIntent(@RequestBody IntentPaymentRequestDTO paymentRequest)
            throws StripeException {
        try {
            String clientSecret = payementService.createPaymentIntent(paymentRequest);
            return ResponseEntity.ok(
                    Map.of("clientSecret", clientSecret));
        } catch (StripeException e) {
            System.err.println("Stripe error: " + e.getMessage());
            return ResponseEntity.status(500).body("Error creating payment intent");
        }
    }

    @PostMapping("/check-payment")
    public ResponseEntity<?> checkPaymentStatus(@RequestBody Map<String, String> request) {
        String paymentIntentId = request.get("paymentIntentId");
        try {
            boolean status = payementService.checkPaymentStatus(paymentIntentId);
            return ResponseEntity.ok(Map.of("status", status));
        } catch (StripeException e) {
            System.err.println("Stripe error: " + e.getMessage());
            return ResponseEntity.status(500).body("Error checking payment status");
        }
    }

}
