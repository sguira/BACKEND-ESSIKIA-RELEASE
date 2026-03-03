package com.formation.demo.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.formation.demo.entities.Planification;
import com.formation.demo.services.PlanificationService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/api/v1/planification")
@RequiredArgsConstructor
public class PlanificationController {

    private final PlanificationService planificationService;

    @PostMapping("")
    public ResponseEntity<Object> createPlanification(@RequestBody Planification planification) {
        try {
            System.out.println("Planification reçue: " + planification);
            Planification createdPlanification = planificationService.planificationSeance(planification);
            return ResponseEntity.ok().body(createdPlanification);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // suppression d'une planification par son ID

    // Liste des planifications pour un formateur spécifique
    @GetMapping("/formateur")
    public ResponseEntity<Iterable<Planification>> getPlanificationsByFormateur(@RequestParam String formateurId) {
        try {
            Iterable<Planification> planifications = planificationService.getPlanificationsByFormateur(formateurId);
            return ResponseEntity.ok(planifications);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // suppression d'une planification par son ID
    @DeleteMapping("/{planificationId}")
    public ResponseEntity<Object> deletePlanificationById(@PathVariable String planificationId) {
        try {
            planificationService.deletePlanificationById(planificationId);
            return ResponseEntity.ok().body("Planification supprimée avec succès");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
