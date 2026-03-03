package com.formation.demo.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.formation.demo.entities.Etudiant;
import com.formation.demo.entities.Matiere;
import com.formation.demo.entities.Seance;
import com.formation.demo.services.MatiereService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/matiere")
@RequiredArgsConstructor
public class MatiereController {

    private final MatiereService matiereService;

    @PostMapping("")
    public ResponseEntity saveMatiere(@RequestBody Matiere matiere) {
        return matiereService.createMatiere(matiere);
    }

    @PutMapping("")
    ResponseEntity<Matiere> updateMatiere(@RequestBody Matiere matiere) {
        return matiereService.updateMatiere(matiere);
    }

    // liste des etudiant inscrit dans une matière donnée
    @GetMapping("/etudiants/{module_id}")
    public ResponseEntity<List<Etudiant>> getEtudiantsByMatiere(@PathVariable String id) {
        return matiereService.getStudentByMatiere(id);
    }

    // Liste de toutes les matières
    @GetMapping("")
    public ResponseEntity<Object> getAllMatieres() {
        return ResponseEntity.ok(matiereService.allMatiere());
    }

    // assigner matières
    @PutMapping("/assigner/{id}")
    public ResponseEntity<Object> assignerMatiere(@PathVariable("idMatiere") String idMatiere,
            @RequestParam("idFormateur") String idProf) {
        return matiereService.assigner(idMatiere, idProf);
    }

}
