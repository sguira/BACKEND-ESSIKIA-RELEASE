package com.formation.demo.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.formation.demo.dto.PromotionDTO;
import com.formation.demo.entities.Promotion;
import com.formation.demo.services.PromotionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/promotion")
@RequiredArgsConstructor
public class PromotionController {

    private final PromotionService promotionService;

    // ─── CRUD de base ─────────────────────────────────────────────────────────

    @PostMapping("")
    public ResponseEntity<?> savePromotion(@RequestBody PromotionDTO dto) {
        return promotionService.createPromotion(dto);
    }

    @GetMapping("")
    public ResponseEntity<Object> allPromotion() {
        return promotionService.listePromotion();
    }

    @PutMapping("")
    public ResponseEntity<Promotion> updatePromotion(@RequestBody Promotion promotion) {
        return promotionService.updatePromotion(promotion);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletePromotion(@PathVariable String id) {
        return promotionService.deletePromotion(id);
    }

    // ─── Endpoints étudiant ───────────────────────────────────────────────────

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Promotion>> getPromotionForUser(@PathVariable String userId) {
        try {
            return ResponseEntity.ok(promotionService.getPromotionForUser(userId));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    // Retourne la promotion active (INSCRIPTION_OUVERTE ou EN_COURS)
    @GetMapping("/active")
    public ResponseEntity<?> getActivePromotion() {
        try {
            Optional<Promotion> active = promotionService.getActivePromotion();
            if (active.isPresent()) {
                return ResponseEntity.ok(active.get());
            }
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    // ─── Liste d'attente globale (implicite) ──────────────────────────────────

    @GetMapping("/liste-attente-globale")
    public ResponseEntity<Object> getListeAttenteGlobale() {
        return promotionService.getListeAttenteGlobale();
    }

    @PostMapping("/liste-attente-globale/{etudiantId}")
    public ResponseEntity<Object> ajouterListeAttenteGlobale(@PathVariable String etudiantId) {
        return promotionService.ajouterListeAttenteGlobale(etudiantId);
    }

    @DeleteMapping("/liste-attente-globale/{etudiantId}")
    public ResponseEntity<Object> retirerListeAttenteGlobale(@PathVariable String etudiantId) {
        return promotionService.retirerListeAttenteGlobale(etudiantId);
    }

    // ─── Liste d'attente (par promotion) ──────────────────────────────────────

    @PostMapping("/{promotionId}/liste-attente/{etudiantId}")
    public ResponseEntity<Object> ajouterListeAttente(
            @PathVariable String promotionId,
            @PathVariable String etudiantId) {
        System.out.println("Promotion ID: " + promotionId);
        System.out.println("Etudiant ID: " + etudiantId);
        return promotionService.ajouterListeAttente(promotionId, etudiantId);
    }

    @DeleteMapping("/{promotionId}/liste-attente/{etudiantId}")
    public ResponseEntity<Object> retirerListeAttente(
            @PathVariable String promotionId,
            @PathVariable String etudiantId) {
        return promotionService.retirerListeAttente(promotionId, etudiantId);
    }

    // ─── Assignation formateur ────────────────────────────────────────────────

    // Assigner un formateur à un module précis dans la promotion
    @PostMapping("/{promotionId}/modules/{moduleId}/formateur/{formateurId}")
    public ResponseEntity<Object> assignerFormateurAModule(
            @PathVariable String promotionId,
            @PathVariable String moduleId,
            @PathVariable String formateurId) {
        return promotionService.assignerFormateurAModule(promotionId, moduleId, formateurId);
    }

    // ─── Progression ──────────────────────────────────────────────────────────

    @GetMapping("/progression/{userId}/{promotionId}")
    public ResponseEntity<Double> getProgression(
            @PathVariable String userId,
            @PathVariable String promotionId) {
        try {
            return ResponseEntity.ok(promotionService.promotionProgression(userId, promotionId));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/send-rappel-begin-promotions/{promotionId}")
    public ResponseEntity sendRappel(
            @PathVariable String promotionId) {

        return ResponseEntity.ok(promotionService.sendRappel(promotionId));

    }

    @PostMapping("/update/{id}")
    ResponseEntity<?> _updatePromotion(@PathVariable String id, @RequestBody Promotion promotion) {
        promotionService.updatePromotion(promotion, id);
        return ResponseEntity.ok("Promotion mise à jour avec succès");
    }

    @PostMapping("re-assign-formateur")
    ResponseEntity<?> _reAssignFormateur(@RequestParam String promotionId, @RequestParam String moduleId,
            @RequestParam String formateurId) {
        promotionService.assignPromotionModule(promotionId, moduleId, formateurId);
        return ResponseEntity.ok("Formateur réassigné avec succès");
    }

}
