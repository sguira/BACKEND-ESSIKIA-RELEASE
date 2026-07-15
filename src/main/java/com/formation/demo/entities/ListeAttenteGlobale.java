package com.formation.demo.entities;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.formation.demo.enumeration.StatutListeGlobale;

import lombok.Data;

@Document(collection = "liste_attente_globale")
@Data
public class ListeAttenteGlobale {

    @Id
    private String id;

    private String etudiantId;
    private StatutListeGlobale statut = StatutListeGlobale.IMPAYE; // "IMPAYE" ou "PAYE"
    private String email;
    private LocalDateTime dateAjout = LocalDateTime.now();
}
