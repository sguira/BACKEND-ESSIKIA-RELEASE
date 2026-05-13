package com.formation.demo.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.formation.demo.config.StripeConfig;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/webhook")
public class WebhookController {

    private final StripeConfig stripeConfig;

    @PostMapping
    public ResponseEntity<String> handleStripeEvent(
            HttpServletRequest request) throws Exception {

        String payload = new String(request.getInputStream().readAllBytes());
        String sigHeader = request.getHeader("Stripe-Signature");

        Event event = Webhook.constructEvent(
                payload,
                sigHeader,
                stripeConfig.getWebhookSecret());

        switch (event.getType()) {

            case "payment_intent.succeeded":
                PaymentIntent paymentIntent = (PaymentIntent) event.getDataObjectDeserializer()
                        .getObject()
                        .orElse(null);

                // 🔥 Ici tu mets ton traitement :
                // - update DB
                // - activer abonnement
                // - générer facture
                System.out.println("Paiement réussi: "
                        + paymentIntent.getId());
                break;

            case "payment_intent.payment_failed":
                System.out.println("Paiement échoué");
                break;
        }

        return ResponseEntity.ok("Success");
    }
}