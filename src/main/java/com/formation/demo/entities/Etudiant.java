package com.formation.demo.entities;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.RequiredArgsConstructor;

// @Data
@Document
@RequiredArgsConstructor
public class Etudiant extends Utilisateur {

    // private boolean confirmed = false;
    // private String codeConfirm = "";

    @DBRef
    private List<Modules> modules = new ArrayList<>();

    @DBRef
    List<Matiere> matieres = new ArrayList<>();

    @DBRef
    List<Note> notes;

    public List<Modules> getModules() {
        return this.modules;
    }

    public List<Matiere> getMatieres() {
        return this.matieres;
    }

    public void follow(Matiere e) {
        this.matieres.add(e);
    }

    public void suscribeModule(Modules mod) {
        this.modules.add(mod);
    }

}