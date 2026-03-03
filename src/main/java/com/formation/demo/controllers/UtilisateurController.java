package com.formation.demo.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.formation.demo.dto.UpdateUserDTO;
import com.formation.demo.entities.Fichiers;
import com.formation.demo.services.UtilisateurService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/api/v1/utilisateurs")
@RequiredArgsConstructor
public class UtilisateurController {

    private final UtilisateurService utilisateurService;

    @PostMapping("/ajouter-avatar/{userId}")
    ResponseEntity ajouterAvatar(@PathVariable String userId, @RequestBody Fichiers avatar) {
        // Logique pour ajouter un avatar à un utilisateur
        try {
            utilisateurService.updateAvatar(userId, avatar);
            return ResponseEntity.ok("Avatar ajouté avec succès.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur lors de l'ajout de l'avatar: " + e.getMessage());
        }
    }

    @PostMapping("/update-user/{userEmail}")
    ResponseEntity<Object> updateUser(@RequestBody UpdateUserDTO user, @PathVariable String userEmail) {
        // Logique pour mettre à jour les informations d'un utilisateur
        System.out.println("Mise à jour de l'utilisateur ID: " + userEmail);
        utilisateurService.updateUser(user, userEmail); // Vous pouvez passer l'ID de l'utilisateur si nécessaire
        return ResponseEntity.ok("Utilisateur mis à jour avec succès.");
    }

}
