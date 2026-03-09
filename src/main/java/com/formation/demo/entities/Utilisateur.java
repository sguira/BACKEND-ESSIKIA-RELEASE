package com.formation.demo.entities;

import java.time.Instant;

// import java.sql.Date;
// import java.util.Date;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.AllArgsConstructor;
// import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Utilisateur {

    @Id
    private String id;
    private String nom;
    private String prenom;
    private String email;
    private String adresse;
    private String password;
    private String telephone;
    private String resetCode;
    private boolean isConfirmed = false;
    private String codeConfirm = "";
    private boolean isBlocked = false;
    private String recupPassword;
    private String role;
    private List<Fichiers> avatars = new ArrayList<>();
    @DBRef
    private List<Promotion> promotions = new ArrayList<>();
    private String type;
    private String objectif;
    private String profession;
    private String defaults;
    private String qualites;

    @DBRef
    private List<Suscription> suscriptions = new ArrayList<>();

    @CreatedDate
    private Instant createdAt;

    public void ajouterAvatar(Fichiers fichiers) {
        this.avatars.add(fichiers);
    }

    public boolean getIsConfirmed() {
        return this.isConfirmed;
    }

    public void setIsConfirmed(boolean isConfirmed) {
        this.isConfirmed = isConfirmed;
    }

    public void setConfirmed(boolean isConfirmed) {
        this.isConfirmed = isConfirmed;
    }

    public void ajouterSuscription(Suscription suscription) {
        this.suscriptions.add(suscription);
    }

    public void ajouterPromotion(Promotion promotion) {
        this.promotions.add(promotion);
    }

    public void removePromotion(Promotion promotion) {
        this.promotions.remove(promotion);
    }

}
