package com.formation.demo.entities;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

// import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Document
// @Data
@RequiredArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Formateur extends Utilisateur {
    private String cin;
    private boolean isBloqued = false;
    private Fichiers profile;
    @DBRef
    private List<Module> modules;

}
