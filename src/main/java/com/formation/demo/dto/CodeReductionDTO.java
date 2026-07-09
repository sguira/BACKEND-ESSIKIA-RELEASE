package com.formation.demo.dto;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.formation.demo.enumeration.TypeReduction;

import lombok.Data;

@Data
public class CodeReductionDTO {
    private String code;
    private TypeReduction typeReduction;
    private double valeur;
    private String offreId; // null = toutes les offres
    private Instant dateExpiration;
    private int nombreUtilisationsMax; // 0 = illimité

    @JsonProperty("isActive")
    private boolean isActive;
}
