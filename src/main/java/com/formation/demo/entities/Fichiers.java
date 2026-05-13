package com.formation.demo.entities;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

// import org.springframework.data.mongodb.core.mapping.DBRef;

// import jakarta.persistence.Id;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Document
@Data
@RequiredArgsConstructor
public class Fichiers implements Serializable {
    @Id
    private String id;
    private String name;
    private String path;
    private String type;
    private int size;
    private String description;
    private String url;

}
