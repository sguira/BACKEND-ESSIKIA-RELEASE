package com.formation.demo.entities;

import java.time.Instant;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

// import jakarta.persistence.Id;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Document
@RequiredArgsConstructor
@Data
public class Message {

    @Id
    private String id;
    private String content;
    @CreatedDate
    private Instant createdAt;

    private String promotion;
    private String groupe;

    @DBRef
    private Utilisateur user;

    private String idModule;
    private boolean status;
    private boolean isDelete = false;
    private String type;
    private Fichiers fichiers;

    // Message épinglé par admin/formateur dans le groupe
    private boolean pinned = false;

}
