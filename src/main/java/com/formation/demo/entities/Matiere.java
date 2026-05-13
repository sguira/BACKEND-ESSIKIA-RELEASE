package com.formation.demo.entities;

import java.time.Instant;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Document
@RequiredArgsConstructor
public class Matiere {

    @Id
    private String id;
    private String nom;
    private String description;
    @CreatedDate
    private Instant createdAt;
    private String updatedAt;

    private int niveau;
    private int nbSeance;

    @DBRef
    private Formateur formateur;

    private String moduleId;

}
