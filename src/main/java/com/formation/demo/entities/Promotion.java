package com.formation.demo.entities;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Document
@RequiredArgsConstructor
@Data
public class Promotion {

    private String id;
    private String name;
    private String description;

    @DBRef
    private Modules module;
    // private List<Matiere> matieres = new ArrayList<>();

    private List<Etudiant> etudiants = new ArrayList<>();
    @DBRef
    private Utilisateur formateur;

    private String startDate;
    private String endDate;

    // public void ajouterMatiere(Matiere matiere) {
    // this.matieres.add(matiere);
    // }

    public void ajouterEtudiant(Etudiant e) {
        this.etudiants.add(e);
    }

}
