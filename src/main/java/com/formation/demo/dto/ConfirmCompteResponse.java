package com.formation.demo.dto;

import com.formation.demo.entities.Utilisateur;

import lombok.Data;

@Data
public class ConfirmCompteResponse {
    private String role;
    private String email;
    private Utilisateur utilisateur;

}
