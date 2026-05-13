package com.formation.demo.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestSuscription {

    @Id
    private String id;
    @DBRef
    private Modules modules;

    @DBRef
    private Etudiant etudiant;

    private boolean status = false;

}
