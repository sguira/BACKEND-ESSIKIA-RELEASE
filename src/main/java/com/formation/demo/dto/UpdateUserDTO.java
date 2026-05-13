package com.formation.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserDTO {

    private String email;
    private String nom;
    private String prenom;
    private String telephone;
    private String adresse;
    private String objectif;
    private String profession;

}
