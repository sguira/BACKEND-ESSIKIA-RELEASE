package com.formation.demo.dto;

import lombok.Data;

@Data
public class CodeReductionValidationRequest {
    private String code;
    private String offreId;
    private double montantOriginal;
}
