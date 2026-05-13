package com.formation.demo.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OffreOption {

    @Id
    private String id;
    private String nom;
    private String description;
    private boolean inclus; // Indique si l'option est incluse dans l'offre ou non

}
