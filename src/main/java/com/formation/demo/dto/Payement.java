package com.formation.demo.dto;

import java.time.Instant;

import org.springframework.data.mongodb.core.mapping.DBRef;

import com.formation.demo.entities.Utilisateur;

import lombok.Data;

@Data
public class Payement {
    private String amount;
    private String currency;
    private String description;
    @DBRef
    private Utilisateur utilisateur;
    private String status;
    private Instant createdAt;
    private String stripePaymentIntentId;
    
}
