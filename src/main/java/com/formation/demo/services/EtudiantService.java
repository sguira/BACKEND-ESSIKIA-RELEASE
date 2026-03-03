package com.formation.demo.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import com.formation.demo.dto.UserFollowPromotionItem;
import com.formation.demo.entities.Etudiant;
import com.formation.demo.entities.Matiere;
import com.formation.demo.entities.Modules;
import com.formation.demo.entities.Promotion;
import com.formation.demo.entities.Seance;
import com.formation.demo.entities.SuiviCours;
import com.formation.demo.entities.Utilisateur;
import com.formation.demo.repository.EtudiantRepo;
import com.formation.demo.repository.MatiereRepo;
import com.formation.demo.repository.ModulesRepository;
import com.formation.demo.repository.PromotionRepository;
import com.formation.demo.repository.SuiviCourRepository;
import com.formation.demo.repository.UtilisateurRepo;

import lombok.RequiredArgsConstructor;

@Service
@Component
@RequiredArgsConstructor
public class EtudiantService {

    private final EtudiantRepo etudiantRepo;
    private final UtilisateurRepo utilisateurRepo;
    private final MatiereRepo matiereRepo;
    private final ModulesRepository modulesRepository;
    private final PromotionRepository promotionRepository;
    private final SeanceService seanceService;
    private final SuiviCourRepository suiviCourRepository;

    public ResponseEntity<Object> getAllEtudiants() {
        return ResponseEntity.ok(etudiantRepo.findAll());
    }

    public ResponseEntity<Object> getUserByEmail(String email) {
        return ResponseEntity.ok(etudiantRepo.findByEmail(email));
    }

    // liste des matieres dont un etudiant suit
    public ResponseEntity getMatiereByStudent(String email) {
        try {
            Etudiant etudiant = etudiantRepo.findByEmail(email);
            return ResponseEntity.ok(etudiant.getMatieres());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Modifier un etudiant
    public ResponseEntity<Etudiant> updateStudent(Etudiant etu) {
        try {
            Etudiant etudiant = etudiantRepo.save(etu);
            return ResponseEntity.ok(etudiant);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

    }

    // suivre une matiere
    public ResponseEntity followedMatiere(String matId, String username) {
        try {
            Matiere mat = matiereRepo.findById(matId).get();
            Etudiant etu = etudiantRepo.findByEmail(username);
            etu.follow(mat);
            etudiantRepo.save(etu);
            return ResponseEntity.ok(etu.getMatieres());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // s'inscrire à un module
    public ResponseEntity<Object> suscribeModule(String modId, String username) {

        try {

            Etudiant e = etudiantRepo.findByEmail(username);
            Modules mod = modulesRepository.findById(modId).get();
            e.suscribeModule(mod);
            etudiantRepo.save(e);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // rejoindre une promotion
    // public ResponseEntity<Object> joinPromotion(String promoId, String username)
    // {
    // try {
    // Etudiant e = etudiantRepo.findByEmail(username);
    // Promotion p = promotionRepository.findById(promoId).get();
    // e.jointPromotion(p);
    // etudiantRepo.save(e);
    // return ResponseEntity.ok().build();
    // } catch (Exception e) {
    // return ResponseEntity.badRequest().build();
    // }
    // }

    // Terminer une séance
    public void completeSeance(String seanceId, String userId, String moduleId) {

        System.out.println("service here" + userId + " " + seanceId + " " + moduleId);

        Utilisateur e = utilisateurRepo.findById(userId).get();
        Modules m = modulesRepository.findById(moduleId).get();
        Seance seance = seanceService.getSeanceById(seanceId);

        SuiviCours suivi = new SuiviCours();
        suivi.setModule(m);
        suivi.setUtilisateur(e);
        suivi.setCompleted(true);
        suivi.setCurrentSeance(seance);
        suivi.setModule(m);
        suivi.setCurrentSeanceIndex(m.getSeances().indexOf(seance));

        suiviCourRepository.save(suivi);

    }

    // Retourner les séances terminées par un étudiant pour un module donné
    public List<Seance> getCompletedSeances(String moduleId, String username) {
        try {
            Etudiant e = etudiantRepo.findByEmail(username);
            List<Seance> completedSeances = new ArrayList<>();
            for (SuiviCours suivi : suiviCourRepository
                    .findByUtilisateurIdAndModuleIdAndIsCompletedTrue(e.getId(), moduleId)) {
                if (suivi.getCurrentSeance() != null) {
                    completedSeances.add(suivi.getCurrentSeance());
                }
            }

            return completedSeances;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Liste des séances terminées par un étudiant
    public List<Seance> getAllCompletedSeances(String email) {
        Utilisateur e = utilisateurRepo.findByEmail(email).orElse(null);
        System.out.println("etudiant " + e.getEmail());
        List<Seance> seances = new ArrayList<Seance>();
        for (SuiviCours suivi : suiviCourRepository.findByUtilisateurIdAndIsCompletedTrue(e.getId())) {
            if (suivi.getCurrentSeance() != null) {
                seances.add(suivi.getCurrentSeance());
            }
        }
        return seances;
    }

    // suivre une promotion et ajouter dans mes promotions
    public ResponseEntity<Object> followPromotion(String promoId, String userId) {
        try {
            Promotion p = promotionRepository.findById(promoId).get();
            Utilisateur e = utilisateurRepo.findById(userId).get();
            e.ajouterPromotion(p);
            utilisateurRepo.save(e);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    // Mes promotions
    public List<UserFollowPromotionItem> getMyPromotions(String userId) {
        Optional<Utilisateur> userOpt = utilisateurRepo.findById(userId);
        Utilisateur e = userOpt.orElseGet(() -> utilisateurRepo.findByEmail(userId).orElse(null));
        if (e == null) {
            return new ArrayList<>();
        }
        List<Promotion> promotions = e.getPromotions();
        List<UserFollowPromotionItem> items = new ArrayList<>();
        if (promotions == null || promotions.isEmpty()) {
            return items;
        }

        List<SuiviCours> completedSuivis = suiviCourRepository.findByUtilisateurIdAndIsCompletedTrue(e.getId());
        Map<String, Integer> completedByModule = new HashMap<>();
        for (SuiviCours suivi : completedSuivis) {
            if (suivi.getModule() != null) {
                completedByModule.merge(suivi.getModule().getId(), 1, Integer::sum);
            }
        }

        Map<String, Integer> totalSeancesByModule = new HashMap<>();
        for (Promotion p : promotions) {
            if (p.getModule() == null) {
                continue;
            }
            String moduleId = p.getModule().getId();
            int nbSeance = totalSeancesByModule.computeIfAbsent(moduleId,
                    id -> seanceService.getSeancesByModule(id).size());
            int nbSeanceComplete = completedByModule.getOrDefault(moduleId, 0);
            UserFollowPromotionItem item = UserFollowPromotionItem.builder()
                    .promotion(p)
                    .nbSeance(nbSeance)
                    .nbSeanceComplete(nbSeanceComplete)
                    .build();
            items.add(item);
        }
        return items;
    }

    private int countCompletedSeancesForPromotion(String userId, Promotion promotion) {
        int count = 0;
        String moduleId = promotion.getModule().getId();
        Utilisateur e = utilisateurRepo.findById(userId).get();
        for (SuiviCours suivi : suiviCourRepository.findAll()) {
            if (suivi.getUtilisateur() != null && suivi.getModule() != null) {
                if (suivi.getUtilisateur().getId().equals(e.getId()) && suivi.getModule().getId().equals(moduleId)
                        && suivi.isCompleted()) {
                    count++;
                }
            }
        }
        return count;
    }

    private int countTotalSeancesForPromotion(Promotion promotion) {
        String moduleId = promotion.getModule().getId();
        return seanceService.getSeancesByModule(moduleId).size();
    }

    // leave promotion
    public ResponseEntity<Object> leavePromotion(String promoId, String userId) {
        try {
            Promotion p = promotionRepository.findById(promoId).get();
            Utilisateur e = utilisateurRepo.findById(userId).get();
            e.removePromotion(p);
            utilisateurRepo.save(e);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

}
