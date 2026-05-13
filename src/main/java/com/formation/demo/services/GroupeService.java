package com.formation.demo.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.formation.demo.entities.Groupe;
import com.formation.demo.entities.Promotion;
import com.formation.demo.repository.GroupeRepository;
import com.formation.demo.repository.PromotionRepository;
import com.formation.demo.repository.SuscriptionRepo;

import lombok.RequiredArgsConstructor;

@Service
@Component
@RequiredArgsConstructor
public class GroupeService {

    private final GroupeRepository groupeRepository;
    private final PromotionRepository promotionRepository;
    
    private final SuscriptionService suscriptionService;

    public ResponseEntity<List<Groupe>> allgroupes() {
        return ResponseEntity.ok(groupeRepository.findAll());
    }

    public ResponseEntity<Groupe> createGroupe(Groupe groupe) {
        try {
            return ResponseEntity.ok(groupeRepository.save(groupe));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    public Groupe promotionGroupe(String id) {
        try {
            Promotion promotion = promotionRepository.findById(id).get();
            return groupeRepository.findGroupeByPromotion(promotion);
        } catch (Exception e) {
            return null;
        }
    }

    public ResponseEntity delete(String id) {
        try {
            groupeRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    public List<Groupe> getGroupesForUser(String userId) {
        List<String> promotionIds = suscriptionService.promotionForStudent(userId);
        List<Groupe> mesgroupes = new ArrayList<Groupe>();
        if (promotionIds.isEmpty()) {
            return List.of();
        }
        for (var item : groupeRepository.findAll()) {
            if (item.getPromotion() != null && promotionIds.contains(item.getPromotion().getId())) {
                mesgroupes.add(item);
            }
        }
        return mesgroupes;
    }

}
