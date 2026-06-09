package com.formation.demo.services;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.formation.demo.dto.PromotionSuscriptionDTO;
import com.formation.demo.dto.SuscriptionDTO;
import com.formation.demo.entities.Etudiant;
import com.formation.demo.entities.Modules;
import com.formation.demo.entities.Offre;
import com.formation.demo.entities.Promotion;
import com.formation.demo.entities.RequestSuscription;
import com.formation.demo.entities.Suscription;
import com.formation.demo.entities.Utilisateur;
import com.formation.demo.repository.EtudiantRepo;
import com.formation.demo.repository.ModulesRepository;
import com.formation.demo.repository.OffreRepo;
import com.formation.demo.repository.PromotionRepository;
import com.formation.demo.repository.RequestSuscribeRepo;
import com.formation.demo.repository.SuscriptionRepo;
import com.formation.demo.repository.UtilisateurRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SuscriptionService {

    private final EtudiantRepo etudiantRepo;
    private final ModulesRepository modulesRepository;
    private final RequestSuscribeRepo requestSuscribeRepo;
    private final SuscriptionRepo suscriptionRepo;
    private final UtilisateurRepo utilisateurRepo;
    private final OffreRepo offreRepo;
    private final PromotionRepository promotionRepository;
    private final PayementService payementService;

    public Suscription suscription(SuscriptionDTO suscription) throws RuntimeException {

        try {
            boolean hasActiveSubscription = suscriptionRepo.findByUtilisateurIdAndModuleId(
                    utilisateurRepo.findByEmail(suscription.getEmail())
                            .orElseThrow(
                                    () -> new RuntimeException("User not found with email: " + suscription.getEmail()))
                            .getId(),
                    null).isPresent();

            boolean valideSuscription = payementService.checkPaymentStatus(suscription.getPaiementIntent());

            if (valideSuscription == false) {
                throw new RuntimeException(
                        "Payment not successful for payment intent: " + suscription.getPaiementIntent());
            }

            if (suscription.getEmail() == null || suscription.getOffreId() == null) {
                throw new RuntimeException("Email and OffreId are required");
            }

            // if (hasActiveSubscription) {
            //     throw new RuntimeException("User already has an active subscription for this module");
            // }

            Utilisateur utilisateur = utilisateurRepo.findByEmail(suscription.getEmail()).orElse(null);
            if (utilisateur == null) {
                throw new RuntimeException("User not found with email: " + suscription.getEmail());
            }
            Offre offre = offreRepo.findById(suscription.getOffreId()).orElse(null);
            if (offre == null) {
                throw new RuntimeException("Offre not found with id: " + suscription.getOffreId());
            }
            Suscription newSuscription = new Suscription();
            newSuscription.setUtilisateurId(utilisateur.getId());
            newSuscription.setOffre(offre);
            newSuscription.setStripeSubscriptionId(suscription.getPaiementIntent());
            newSuscription.setStartDate(Instant.now());
            newSuscription.setPlan(offre.getNom());
            newSuscription.setStatus("active");
            newSuscription.setPrice(offre.getPrix());
            newSuscription.setEmail(suscription.getEmail());
            return suscriptionRepo.save(newSuscription);
        } catch (Exception e) {
            throw new RuntimeException("Error processing subscription: " + e.getMessage());
        }
    }

    public boolean isSubscribed(String userId, String moduleId) {
        Utilisateur utilisateur = utilisateurRepo.findById(userId).orElse(null);
        List<Suscription> suscriptions = utilisateur != null ? utilisateur.getSuscriptions() : null;
        if (suscriptions == null || suscriptions.isEmpty()) {
            return false;
        }
        for (Suscription suscription : suscriptions) {
            if ((suscription.getModuleId().equals(moduleId) || "premium".equals(suscription.getPlan()))
                    && "active".equals(suscription.getStatus()) && suscription.getUtilisateurId().equals(userId)) {
                return true;
            }
        }

        return false;
    }

    public boolean hasSuscribepromotion(String userId, String promotionId) {
        List<Suscription> suscriptions = suscriptionRepo.findAll();
        for (Suscription sous : suscriptions) {
            if (sous.getPromotionId() != null) {
                if (sous.getPromotionId().equals(promotionId) && sous.getUtilisateurId().equals(userId)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void addPromotionSouscription(PromotionSuscriptionDTO pSuscriptionDTO) {

        Utilisateur utilisateur = utilisateurRepo.findById(pSuscriptionDTO.getUtilisateurId()).orElse(null);

        if (utilisateur == null) {
            System.out.println("User not found with ID: " + pSuscriptionDTO.getUtilisateurId());
            return;
        }

        Suscription suscription = new Suscription();
        suscription.setPromotionId(pSuscriptionDTO.getPromotionId());
        suscription.setUtilisateurId(utilisateur.getId());
        suscription.setStartDate(Instant.now());
        suscriptionRepo.save(suscription);

    }

    // ── Bypass test — souscription sans vérification Stripe ──────────────────
    // À n'utiliser qu'en environnement de test (bypassPayment = true côté Flutter).
    public Suscription bypassSuscription(String email, String offreId) {
        Utilisateur utilisateur = utilisateurRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable : " + email));

        Offre offre = (offreId != null && !offreId.isBlank())
                ? offreRepo.findById(offreId).orElse(null)
                : offreRepo.findAll().stream().findFirst().orElse(null);

        if (offre == null) {
            throw new RuntimeException("Aucune offre disponible pour le bypass");
        }

        // Si une souscription active existe déjà, on la retourne directement
        Suscription existante = suscriptionRepo
                .findTopByUtilisateurIdOrderByStartDateDesc(utilisateur.getId())
                .orElse(null);
        if (existante != null && "active".equals(existante.getStatus())) {
            System.out.println("[Bypass] Souscription active existante pour " + email);
            return existante;
        }

        Suscription suscription = new Suscription();
        suscription.setUtilisateurId(utilisateur.getId());
        suscription.setEmail(email);
        suscription.setOffre(offre);
        suscription.setStripeSubscriptionId("bypass_test");
        suscription.setStartDate(Instant.now());
        suscription.setPlan(offre.getNom());
        suscription.setStatus("active");
        suscription.setPrice(0.0);
        System.out.println("[Bypass] Souscription créée sans paiement pour " + email + " — offre : " + offre.getNom());
        return suscriptionRepo.save(suscription);
    }

    // ── Bypass test — rejoindre une promotion sans paiement ───────────────────
    public void bypassPromotion(String utilisateurId, String promotionId) {
        Utilisateur utilisateur = utilisateurRepo.findById(utilisateurId)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable : " + utilisateurId));

        Promotion promotion = promotionRepository.findById(promotionId)
                .orElseThrow(() -> new RuntimeException("Promotion introuvable : " + promotionId));

        // Vérifier si déjà inscrit via la liste embarquée dans le user
        boolean dejaInscrit = utilisateur.getPromotions() != null &&
                utilisateur.getPromotions().stream()
                        .anyMatch(p -> promotionId.equals(p.getId()));
        if (dejaInscrit) {
            System.out.println("[Bypass] Déjà inscrit à la promotion " + promotionId);
            return;
        }

        // 1. Créer la souscription (pour que fetchMesPromotions via suscriptionRepo
        // fonctionne)
        Suscription suscription = new Suscription();
        suscription.setUtilisateurId(utilisateur.getId());
        suscription.setEmail(utilisateur.getEmail());
        suscription.setPromotionId(promotionId);
        suscription.setStartDate(Instant.now());
        suscription.setStatus("active");
        suscriptionRepo.save(suscription);

        // 2. Ajouter la promotion dans utilisateur.promotions (pour que
        // getGroupesForUser fonctionne)
        utilisateur.ajouterPromotion(promotion);
        utilisateurRepo.save(utilisateur);

        System.out.println("[Bypass] Inscription promotion " + promotionId + " pour " + utilisateur.getEmail());
    }

    public List<String> promotionForStudent(String id) {
        // List<String> promotionIds = new ArrayList<>();
        // for (var item : suscriptionRepo.findAll()) {
        // if (item.getUtilisateurId().equals(id) && item.getPromotionId() != null) {
        // promotionIds.add(item.getPromotionId());
        // }
        // }
        // return promotionIds;
        Utilisateur utilisateur = utilisateurRepo.findById(id).orElse(null);

        List<String> promotionIds = new ArrayList<>();
        for (var p : utilisateur.getPromotions()) {
            promotionIds.add(p.getId());
        }
        return promotionIds;
    }

    public boolean getUserSuscriptions(String email) {
        Utilisateur utilisateur = utilisateurRepo.findById(email).orElse(null);
        if (utilisateur == null) {
            System.out.println("User not found with ID: " + email);
            return false;
        }
        System.out.println("User found: " + utilisateur.getEmail() + ", ID: " + utilisateur.getId());
        List<Suscription> userSuscriptions = new ArrayList<>();
        for (Suscription suscription : suscriptionRepo.findAll()) {
            if (suscription.getUtilisateurId().equals(utilisateur.getId())) {
                System.out.println(
                        "Found subscription for user ID: " + utilisateur.getId() + ", Subscription ID: "
                                + suscription.getId());
                return true;
            }
        }
        System.out.println("No subscriptions found for user ID: " + utilisateur.getId());
        return false;
    }

    public Offre getUserOffre(String email) {
        Utilisateur utilisateur = utilisateurRepo.findByEmail(email).orElse(null);
        if (utilisateur == null) {
            return null;
        }
        // Skip promotion-only subscriptions (offre == null) — always return the latest
        // paid/bypass subscription that actually has an offer attached.
        return suscriptionRepo.findByUtilisateurIdOrderByStartDateDesc(utilisateur.getId())
                .stream()
                .filter(s -> s.getOffre() != null && "active".equals(s.getStatus()))
                .map(Suscription::getOffre)
                .findFirst()
                .orElse(null);
    }

}
