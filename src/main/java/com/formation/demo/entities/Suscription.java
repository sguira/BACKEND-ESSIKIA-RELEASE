package com.formation.demo.entities;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Document
@Data
public class Suscription {

    @Id
    private String id;
    private String utilisateurId;
    private String moduleId;
    private String promotionId;
    private String plan = "STANDARD";
    private String status;
    private Instant startDate;
    private Instant endDate;
    private double price;
    private String stripeSubscriptionId;
    @NotNull
    private String email;
    @DBRef
    private Offre offre;

}
