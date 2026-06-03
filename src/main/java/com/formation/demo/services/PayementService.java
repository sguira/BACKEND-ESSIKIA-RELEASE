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
            long amount = Math.round(Double.parseDouble(paymentRequest.getAmount()) * 100); // centimes
            System.out.println("Amount (centimes): " + amount);
            Map<String, Object> params = new HashMap<>();
            params.put("amount", amount);
            params.put("currency", paymentRequest.getCurrency() != null ? paymentRequest.getCurrency() : "eur");
            params.put("automatic_payment_methods", Map.of("enabled", true));
            PaymentIntent paymentIntent = PaymentIntent.create(params);
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
