package com.formation.demo.controllers;

import java.util.List;

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

    @PostMapping("")
    public ResponseEntity<?> savePromotion(@RequestBody PromotionDTO promotion) {
        return promotionService.createPromotion(promotion);
    }

    @GetMapping("")
    public ResponseEntity<Object> allPromotion() {
        return promotionService.listePromotion();
    }

    @PutMapping("")
    public ResponseEntity<Promotion> updatePromotion(@RequestBody Promotion promotion) {
        return promotionService.updatePromotion(promotion);
    }

    @DeleteMapping("")
    public ResponseEntity<Object> deletePromotion(@PathVariable String id) {
        return promotionService.deletePromotion(id);
    }

    // @PostMapping("/assign")
    // public ResponseEntity<Object> assignerMatiere(@RequestParam("idFormateur")
    // String idFormateur,
    // @RequestParam("idMatiere") String idMatiere, @RequestParam("idPromotion")
    // String idPromotion) {

    // // return promotionService.assigneMatiere(idFormateur, idPromotion,
    // idMatiere);
    // }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Promotion>> getPromotionForUser(
            @PathVariable String userId) {
        try {
            return ResponseEntity.ok(promotionService.getPromotionForUser(userId));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/progression/{userId}/{promotionId}")
    public ResponseEntity<Double> getPromotionForUser(
            @PathVariable String userId, @PathVariable String promotionId) {
        try {
            return ResponseEntity.ok(promotionService.promotionProgression(userId, promotionId));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/assign-formateur")
    ResponseEntity<Object> assignerProfToModule(@RequestParam("promotionId") String promotionId,
            @RequestParam("formateurId") String formateurId) {
        try {
            promotionService.assignerProfToModule(promotionId, formateurId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

}
