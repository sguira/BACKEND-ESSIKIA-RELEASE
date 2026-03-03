package com.formation.demo.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.formation.demo.dto.BloqueFormateurDTO;
import com.formation.demo.entities.Formateur;
import com.formation.demo.services.AuthService;
import com.formation.demo.services.FormateurService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1/formateur")
@RequiredArgsConstructor
public class FormateurController {

    private final AuthService authService;
    private final FormateurService formateurService;

    @PostMapping("")
    public ResponseEntity<Object> postMethodName(@RequestBody Formateur formateur) {
        // TODO: process POST request

        return authService.createFormateur(formateur);
    }

    @GetMapping("")
    ResponseEntity<Object> listeFormateur() {
        return formateurService.listeFormateur();
    }

    @GetMapping("/promotions")
    ResponseEntity<Object> listePromotions(@RequestParam("idFormateur") String id) {
        return ResponseEntity.ok().body(formateurService.listesDesPromotions(id));
    }

    // retourner la liste des groupes d'un formateur
    @GetMapping("/groupes/{idFormateur}")
    ResponseEntity<Object> listeGroupes(@PathVariable("idFormateur") String id) {
        return ResponseEntity.ok().body(formateurService.listesDesGroupes(id));
    }

    // bloquer un enseignant
    @PostMapping("/bloquer")
    ResponseEntity<?> bloquer(@RequestBody BloqueFormateurDTO bloqueFormateurDTO) {
        formateurService.bloquerFormateur(bloqueFormateurDTO);
        return ResponseEntity.ok("Le formateur a été bloqué");
    }

    @PostMapping("/debloquer")
    ResponseEntity<?> debloquer(@RequestBody BloqueFormateurDTO bloqueFormateurDTO) {
        formateurService.debloquerFormateur(bloqueFormateurDTO);
        return ResponseEntity.ok("Le formateur a été débloqué");
    }

    // @GetMapping("/matieres")
    // ResponseEntity<Object> listeMatiere(@RequestParam("idFormateur") String id) {
    // return formateurService.listesDesMatiere(id);
    // }

}