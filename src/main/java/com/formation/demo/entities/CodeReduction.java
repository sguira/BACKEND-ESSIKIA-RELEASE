
package com.formation.demo.entities;

import java.time.Instant;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.formation.demo.enumeration.TypeReduction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "codes_reduction")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CodeReduction {

    @Id
    private String id;

    @Indexed(unique = true)
    private String code;

    private TypeReduction typeReduction;

    private double valeur;

    private String offreId; // null = valable sur toutes les offres

    private Instant dateExpiration;

    private int nombreUtilisationsMax; // 0 = illimité

    private int nombreUtilisations;

    @JsonProperty("isActive")
    private boolean isActive;

    @CreatedDate
    private Instant createdAt;
}
