package com.formation.demo.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.formation.demo.dto.AssignMatProf;
import com.formation.demo.dto.PromotionDTO;
import com.formation.demo.entities.Formateur;
import com.formation.demo.entities.Groupe;
import com.formation.demo.entities.Matiere;
import com.formation.demo.entities.Modules;
import com.formation.demo.entities.Promotion;
import com.formation.demo.entities.Utilisateur;
import com.formation.demo.repository.FormateurRepo;
import com.formation.demo.repository.GroupeRepository;
import com.formation.demo.repository.MatiereRepo;
import com.formation.demo.repository.ModulesRepository;
import com.formation.demo.repository.PromotionRepository;
import com.formation.demo.repository.SuiviCourRepository;
import com.formation.demo.repository.SuscriptionRepo;
import com.formation.demo.repository.UtilisateurRepo;

import lombok.RequiredArgsConstructor;

@Service
@Component
@RequiredArgsConstructor
public class PromotionService {

    private final GroupeRepository groupeRepository;

    // private final PromotionService promotionService;
    private final PromotionRepository promotionRepository;
    private final MatiereRepo matiereRepo;
    private final SuscriptionRepo souscriptionRepo;
    private final FormateurRepo formateurRepo;
    private final ModulesRepository modulesRepository;
    private final SuiviCourRepository suiviCourRepository;
    private final UtilisateurRepo utilisateurRepo;
    private final PromotionRepository promotionRepo;
    private final GroupeService groupeService;

    public ResponseEntity<?> createPromotion(PromotionDTO promotionDTO) {
        try {

            Promotion promotion = promotionDTO.getPromotion();

            // for (AssignMatProf ele : promotionDTO.getMatieres()) {
            // Matiere mat = matiereRepo.findById(ele.getIdMatiere()).get();
            // if (ele.getIdFormateur() != null) {
            // mat.setFormateur(formateurRepo.findById(ele.getIdFormateur()).get());
            // }
            // promotion.ajouterMatiere(mat);
            // }

            Promotion promotion2 = promotionRepository.save(promotion);
            // promotion2.setModule(modulesRepository.findById(null));

            Groupe groupe = new Groupe();

            groupe.setPromotion(promotion2);
            groupe.setDescriptions(String.format(
                    "Groupe de discussion de la promotion %s ce groupe permettra aux étudiants et professeur de discuté pour faciliter les échanges entre enseignant et étudiant",
                    promotion2.getName()));
            groupe.setName(String.format("Groupe %s", promotion2.getName()));
            groupeRepository.save(groupe);
            return ResponseEntity.ok(promotion2);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    // modifier une promotion
    public ResponseEntity<Promotion> updatePromotion(Promotion promotion) {
        try {
            return ResponseEntity.ok(promotionRepository.save(promotion));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // supprimer une promotion
    public ResponseEntity<Object> deletePromotion(String id) {
        try {
            promotionRepository.deleteById(id);
            return ResponseEntity.ok("Promotion supprimé");
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // List des promotion
    public ResponseEntity<Object> listePromotion() {
        try {
            return ResponseEntity.ok(promotionRepository.findAll());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // assigne une matière à un prof
    // public ResponseEntity<Object> assigneMatiere(String idFormateur, String
    // idPromotion, String idMatieres) {

    // try {
    // Promotion promotion = promotionRepository.findById(idPromotion).get();
    // if (promotion == null) {
    // return ResponseEntity.status(400).body("La promotion n'éxiste pas");
    // }
    // Formateur formateur = formateurRepo.findById(idFormateur).get();
    // if (formateur == null) {
    // return ResponseEntity.status(400).body("Le formateur n'éxiste pas");
    // }
    // for (var i = 0; i < promotion.getMatieres().size(); i++) {
    // if (promotion.getMatieres().get(i).getId().equals(idMatieres)) {
    // if (promotion.getMatieres().get(i).getFormateur() != null) {
    // return ResponseEntity.status(400).body("La matière est deja assigné a un
    // autre formateur");
    // } else {
    // promotion.getMatieres().get(i).setFormateur(formateur);
    // }
    // }
    // }
    // promotionRepository.save(promotion);
    // return ResponseEntity.status(200).body("Matière bien assignéé");

    // } catch (Exception e) {
    // e.printStackTrace();
    // return ResponseEntity.badRequest().build();
    // }

    // }

    public List<Promotion> getPromotionForUser(
            String userId) {
        List<String> souscribrPromotionId = new ArrayList<>();
        // Récupérer les inscriptions de l'utilisateur
        for (var item : souscriptionRepo.findAll()) {
            if (item.getUtilisateurId().equals(userId)) {
                souscribrPromotionId.add(item.getPromotionId());
            }
        }
        List<Promotion> promotions = new ArrayList<>();
        // Récupérer les promotions associées aux inscriptions
        for (var promoId : souscribrPromotionId) {
            for (var promo : promotionRepository.findAll()) {
                if (promo.getId().equals(promoId)) {

                    promotions.add(promo);
                }
            }
        }
        return promotions;
    }

    public double promotionProgression(String userId, String promotionId) {
        try {
            // recherche de la promotion
            Promotion promotion = promotionRepository.findById(promotionId).orElse(null);
            if (promotion == null) {
                return 0;
            }
            Modules module = modulesRepository.findById(promotion.getModule().getId()).get();
            if (module == null) {
                return 0;
            }
            int nombreSeance = module.getSeances().size();
            int totalSeanceSuivie = 0;
            for (var suivi : suiviCourRepository.findAll()) {
                if (suivi.getUtilisateur().equals(userId) && suivi.getModule().equals(module.getId())) {
                    totalSeanceSuivie++;
                }
            }
            if (nombreSeance == 0 || totalSeanceSuivie == 0) {
                return 0;
            }
            return (totalSeanceSuivie * 100) / nombreSeance;
        } catch (Exception e) {
            return 0;
        }
    }

    // assigner un formateur a un module

    public void assignerProfToModule(String promotionId, String formateurId) {
        Promotion promotion = promotionRepo.findById(promotionId).orElse(null);
        Utilisateur formateur = formateurRepo.findById(formateurId).orElse(null);
        System.out.println("Promotion: " + promotion);
        System.out.println("Formateur: " + formateur);
        if (promotion != null && formateur != null) {
            promotion.setFormateur(formateur);
            promotionRepo.save(promotion);
        }
    }

    // récupération du groupe d'une promotion
    public Groupe getgroupeBypromotionId(String id) {
        final Groupe groupe = groupeService.promotionGroupe(id);
        return groupe;
    }

}
