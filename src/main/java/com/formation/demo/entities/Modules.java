package com.formation.demo.entities;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@Document
public class Modules {

    private String id;
    private String nom;
    private String description;
    private double montant;
    @CreatedDate
    private Instant createdAt;
    private Fichiers miniature;
    private String categorie;
    private int nbSemaine;
    @DBRef
    private List<Matiere> matieres;

    private List<Seance> seances = new ArrayList<>();

    public void ajouterSeance(Seance seance) {
        this.seances.add(seance);
    }

    public void addSeanceWithIndex(Seance seance, int index) {
        this.seances.add(index, seance);
    }

    public void ajouterMatiere(Matiere matiere) {
        this.matieres.add(matiere);
    }

    // private Formateur formateur;
}
