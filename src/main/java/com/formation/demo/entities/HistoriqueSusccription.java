package com.formation.demo.entities;

import java.time.Instant;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.formation.demo.enumeration.SuscriptionStatus;

import lombok.Data;

@Data
@Document(collection = "historique_subscription")
public class HistoriqueSusccription {
    @Id
    private String id;
    private String email;
    @DBRef
    private Offre offre;
    private SuscriptionStatus abonnementStatus;
    @CreatedDate
    private Instant createdAt;
}
