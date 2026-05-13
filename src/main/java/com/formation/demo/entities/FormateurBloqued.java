package com.formation.demo.entities;

import java.time.Instant;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Data;

@Data
@Document
@Builder
public class FormateurBloqued {

    @Id
    private String id;
    @DBRef
    private Formateur formateur;
    private String motif;
    @CreatedDate
    private Instant dateBloquage;

}
