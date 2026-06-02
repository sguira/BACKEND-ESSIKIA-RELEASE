package com.formation.demo.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class PromotionDTO {

    private String name;
    private String description;

    // IDs des modules inclus dans la promotion (avec formateur optionnel)
    private List<PromotionModuleDTO> modules = new ArrayList<>();

    private LocalDate dateInscription;
    private int joursAvantLancement;
    private LocalDate dateDebut;
    private LocalDate dateFin;

    @Data
    public static class PromotionModuleDTO {
        private String moduleId;
        private String formateurId; // optionnel à la création
    }
}
