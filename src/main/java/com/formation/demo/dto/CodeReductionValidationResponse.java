package com.formation.demo.dto;

import com.formation.demo.enumeration.TypeReduction;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CodeReductionValidationResponse {
    private boolean valid;
    private String message;
    private String codeId;
    private TypeReduction typeReduction;
    private double valeur;
    private double montantOriginal;
    private double montantApresReduction;
}
