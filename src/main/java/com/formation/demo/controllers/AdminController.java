package com.formation.demo.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.formation.demo.dto.ValidationPlanification;
import com.formation.demo.entities.Etudiant;
import com.formation.demo.entities.Formateur;
import com.formation.demo.entities.Planification;
import com.formation.demo.services.AdminService;
import com.formation.demo.services.EtudiantService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/api/v1/admin")
@RestController
@RequiredArgsConstructor
public class AdminController {

    private final EtudiantService etudiantService;
    private final AdminService adminService;

    @GetMapping("/student")
    public ResponseEntity<Object> getAllEtudiants() {
        return ResponseEntity.ok(etudiantService.getAllEtudiants());
    }

    @PostMapping("/validation-planification")
    public ResponseEntity<Object> validationPlanification(
            @RequestBody ValidationPlanification validationPlanification) {
        try {
            return ResponseEntity.ok(adminService.validationPlanification(validationPlanification));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/planifications")
    public ResponseEntity<List<Planification>> getAllPlanifications() {
        return ResponseEntity.ok(adminService.getAllPlanifications());
    }

    @PostMapping("/annulation-planification")
    public ResponseEntity<Object> annulationPlanification(
            @RequestBody ValidationPlanification validationPlanification) {
        try {
            Planification planification = adminService
                    .anuulerPlanification(validationPlanification.getPlanificationId());
            return ResponseEntity.ok(planification);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/bloquer-etudiant")
    public ResponseEntity<Object> bloquerEtudiant(@RequestBody Etudiant etudiant) {
        try {
            Etudiant updatedEtudiant = adminService.bloquerEtudiant(etudiant.getEmail());
            return ResponseEntity.ok(updatedEtudiant);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/debloquer-etudiant")
    public ResponseEntity<Object> debloquerEtudiant(@RequestBody Etudiant etudiant) {
        try {
            Etudiant updatedEtudiant = adminService.debloquerEtudiant(etudiant.getEmail());
            return ResponseEntity.ok(updatedEtudiant);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/supprimer-formateur")
    public ResponseEntity<Object> supprimerFormateur(@RequestBody String email) {
        try {
            adminService.suppressionFormateur(email);
            return ResponseEntity.ok("Formateur supprimé avec succès");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/update-formateur")
    public ResponseEntity<Object> updateFormateur(@RequestBody Formateur formateur) {
        try {
            Formateur updatedFormateur = adminService.updFormateur(formateur);
            return ResponseEntity.ok(updatedFormateur);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
