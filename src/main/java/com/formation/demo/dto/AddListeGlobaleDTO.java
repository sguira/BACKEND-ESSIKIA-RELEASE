package com.formation.demo.dto;

import com.formation.demo.enumeration.StatutListeGlobale;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class AddListeGlobaleDTO {

    private final String etudiantId;
    private final String email;
    private final StatutListeGlobale statut; // "IMPAYE" ou "PAYE"

}
