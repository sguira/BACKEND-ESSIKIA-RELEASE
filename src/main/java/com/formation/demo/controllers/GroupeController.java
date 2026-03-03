package com.formation.demo.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.formation.demo.entities.Groupe;
import com.formation.demo.services.GroupeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/groupe")
@RequiredArgsConstructor
public class GroupeController {

    private final GroupeService groupeService;

    // creer un groupe
    @PostMapping("")
    public ResponseEntity<Object> saveGroupe(@RequestBody Groupe groupe) {
        return ResponseEntity.ok(groupeService.createGroupe(groupe));
    }

    // suprimer un groupe
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteGroupe(@PathVariable String id) {
        return ResponseEntity.ok(groupeService.delete(id));
    }

    // tous les groupes
    @GetMapping("/all")
    public ResponseEntity<List<Groupe>> getAllGroupe() {
        return groupeService.allgroupes();
    }

    // groupe pour une promotion
    @GetMapping("/promotion/{promotionId}")
    public ResponseEntity<Groupe> getGroupeByPromotion(@PathVariable String promotionId) {
        Groupe groupe = groupeService.promotionGroupe(promotionId);
        if(groupe == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(groupe);
    }

    // Mes groupes
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Groupe>> getGroupesForUser(@PathVariable String userId) {
        try {
            List<Groupe> mesgroupes = groupeService.getGroupesForUser(userId);
            return ResponseEntity.ok(mesgroupes);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

}
