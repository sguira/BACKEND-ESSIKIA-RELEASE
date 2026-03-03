package com.formation.demo.entities;

import java.sql.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Document
@Data
@RequiredArgsConstructor
public class Evaluation {
    @Id
    private String id;
    private String createdAt = "";
    private String updatedAt = "";
    private List<Fichiers> files;
    @DBRef
    private Formateur formateur;

    @DBRef
    private List<Note> notes;
}
