package com.formation.demo.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.formation.demo.dto.IntentPaymentRequestDTO;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

@Service
public class PayementService {

    public String createPaymentIntent(IntentPaymentRequestDTO paymentRequest)
            throws StripeException {
        try {
            System.out.println("Amount: " + paymentRequest.getAmount());
            Map<String, Object> params = new HashMap<>();
            params.put("amount", 50);
            params.put("currency", "eur");
            params.put("automatic_payment_methods", Map.of("enabled", true));
            PaymentIntent paymentIntent = PaymentIntent.create(params);
            System.out.println("Payment Intent created: " + paymentIntent.getId());
            System.out.println("Client Secret: " + paymentIntent.getClientSecret());
            return paymentIntent.getClientSecret();
        } catch (StripeException e) {
            System.err.println("Stripe error: " + e.getMessage());
            throw e;
        }
    }

    public boolean checkPaymentStatus(String paymentIntentId) throws StripeException {
        try {
            PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
            if ("succeeded".equals(paymentIntent.getStatus())) {
                return true;
            } else {
                System.out.println("Payment status for intent " + paymentIntentId + ": " + paymentIntent.getStatus());
            }
            return false;
        } catch (StripeException e) {
            System.err.println("Stripe error: " + e.getMessage());
            throw e;
        }
    }

}
