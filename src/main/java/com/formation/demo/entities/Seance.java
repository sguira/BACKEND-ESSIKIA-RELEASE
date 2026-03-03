package com.formation.demo.entities;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Document
@RequiredArgsConstructor
public class Seance {

    @Id
    private String id;
    private LocalDate date = LocalDate.now();
    private String title;
    private String description;

    private List<Fichiers> files = new ArrayList<Fichiers>();
    private List<Map<String, Object>> content;
    @DBRef
    private Formateur formateur;
    @DBRef
    private Matiere formation;

    private Fichiers videoQuickOff;

    @DBRef
    private Modules module;

    public void ajouterFichier(Fichiers fichiers) {
        this.files.add(fichiers);
    }

    public void supprimerFichier(Fichiers fichiers) {
        this.files.remove(fichiers);
    }
}
