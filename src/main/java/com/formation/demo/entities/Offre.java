package com.formation.demo.entities;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.formation.demo.enumeration.TypeOffre;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Offre {

    @Id
    private String id;

    private String nom;
    private String description;
    private TypeOffre typeOffre;

    private double prix;
    private String devise = "EUR"; // Devise par défaut
    private int nombrePaiements; // Nombre de paiements (1 pour un paiement unique, plusieurs pour paiements
                                 // échelonnés)
    private int dureeEnMois; // Durée de validité de l'offre en mois

    private boolean isActive = true;
    private boolean isPopulaire = false;
    private int privilege;

    @DBRef
    private List<OffreOption> options = new ArrayList<>();

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

}
