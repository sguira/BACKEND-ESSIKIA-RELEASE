package com.formation.demo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.stripe.Stripe;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "stripe")
@Data
public class StripeConfig {

    private String secretKey;
    private String webhookSecret;

    public void init() {
        // Initialize Stripe with the secret key
        Stripe.apiKey = secretKey;
    }

}
