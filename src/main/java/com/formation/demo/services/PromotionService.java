package com.formation.demo.services;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.formation.demo.dto.PromotionDTO;
import com.formation.demo.email.BodyEmail;
import com.formation.demo.email.EmailServiceImp;
import com.formation.demo.email.EmailTemplates;
import com.formation.demo.entities.Etudiant;
import com.formation.demo.entities.Formateur;
import com.formation.demo.entities.Groupe;
import com.formation.demo.entities.Modules;
import com.formation.demo.entities.Promotion;
import com.formation.demo.entities.PromotionModule;
import com.formation.demo.entities.Utilisateur;
import com.formation.demo.enumeration.PromotionStatus;
import com.formation.demo.repository.EtudiantRepo;
import com.formation.demo.repository.FormateurRepo;
import com.formation.demo.repository.GroupeRepository;
import com.formation.demo.repository.ModulesRepository;
import com.formation.demo.repository.PromotionRepository;
import com.formation.demo.repository.SuiviCourRepository;
import com.formation.demo.repository.SuscriptionRepo;
import com.formation.demo.repository.UtilisateurRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PromotionService {

    private final PromotionRepository promotionRepository;
    private final ModulesRepository modulesRepository;
    private final FormateurRepo formateurRepo;
    private final EtudiantRepo etudiantRepo;
    private final SuscriptionRepo souscriptionRepo;
    private final SuiviCourRepository suiviCourRepository;
    private final GroupeRepository groupeRepository;
    private final GroupeService groupeService;
    private final EmailServiceImp emailService;
    private final UtilisateurRepo utilisateurRepo;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // ─── Création ─────────────────────────────────────────────────────────────

    public ResponseEntity<?> createPromotion(PromotionDTO dto) {
        try {
            Promotion promotion = new Promotion();
            promotion.setName(dto.getName());
            promotion.setDescription(dto.getDescription());
            promotion.setDateInscription(dto.getDateInscription());
            promotion.setJoursAvantLancement(dto.getJoursAvantLancement());
            promotion.setDateDebut(dto.getDateDebut());
            promotion.setDateFin(dto.getDateFin());

            // Construction des modules de la promotion
            for (PromotionDTO.PromotionModuleDTO pmDto : dto.getModules()) {
                Modules module = modulesRepository.findById(pmDto.getModuleId()).orElse(null);
                if (module == null)
                    continue;
                Formateur formateur = null;
                if (pmDto.getFormateurId() != null && !pmDto.getFormateurId().isBlank()) {
                    formateur = formateurRepo.findById(pmDto.getFormateurId()).orElse(null);
                }
                promotion.ajouterModule(new PromotionModule(module, formateur));
            }

            // Calcul du statut initial
            promotion.setStatus(promotion.calculerStatut());

            Promotion saved = promotionRepository.save(promotion);

            // Création du groupe de discussion
            Groupe groupe = new Groupe();
            groupe.setPromotion(saved);
            groupe.setName(String.format("Groupe %s", saved.getName()));
            groupe.setDescriptions(String.format(
                    "Groupe de discussion de la promotion %s — espace d'échange entre étudiants et formateurs.",
                    saved.getName()));
            groupeRepository.save(groupe);

            // Notification de la liste d'attente des promotions terminées
            notifierListeAttente(saved);

            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Erreur lors de la création de la promotion");
        }
    }

    // Notifie tous les étudiants en liste d'attente des anciennes promotions
    private void notifierListeAttente(Promotion nouvellePromotion) {
        if (nouvellePromotion.getDateInscription() == null)
            return;
        String dateInscription = nouvellePromotion.getDateInscription().format(DATE_FORMATTER);
        String dateDebut = nouvellePromotion.getDateDebut() != null
                ? nouvellePromotion.getDateDebut().format(DATE_FORMATTER)
                : "À définir";
        String dateFin = nouvellePromotion.getDateFin() != null
                ? nouvellePromotion.getDateFin().format(DATE_FORMATTER)
                : "À définir";

        for (Promotion ancienne : promotionRepository.findAll()) {
            if (ancienne.getId().equals(nouvellePromotion.getId()))
                continue;
            if (ancienne.getListeAttente() == null || ancienne.getListeAttente().isEmpty())
                continue;
            for (String etudiantId : ancienne.getListeAttente()) {
                Etudiant etudiant = etudiantRepo.findById(etudiantId).orElse(null);
                if (etudiant == null || etudiant.getEmail() == null)
                    continue;
                String html = EmailTemplates.nouvellePromotionListeAttente(
                        etudiant.getPrenom() + " " + etudiant.getNom(),
                        nouvellePromotion.getName(),
                        dateInscription, dateDebut, dateFin);
                BodyEmail body = new BodyEmail(
                        "Nouvelle promotion ESSIKIA — tu es prioritaire !",
                        "", etudiant.getEmail(), null);
                emailService.sendHtlmlMail(body, html);
            }
        }
    }

    // ─── Lecture ──────────────────────────────────────────────────────────────

    public ResponseEntity<Object> listePromotion() {
        try {
            return ResponseEntity.ok(promotionRepository.findAll());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    public Promotion getById(String id) {
        return promotionRepository.findById(id).orElse(null);
    }

    // Retourne la promotion active : INSCRIPTION_OUVERTE en priorité, sinon
    // EN_COURS
    public Optional<Promotion> getActivePromotion() {
        List<Promotion> all = promotionRepository.findAll();
        return all.stream()
                .filter(p -> p.getStatus() == PromotionStatus.INSCRIPTION_OUVERTE)
                .findFirst()
                .or(() -> all.stream()
                        .filter(p -> p.getStatus() == PromotionStatus.EN_COURS)
                        .findFirst());
    }

    public List<Promotion> getPromotionForUser(String userId) {
        // Fusionner les deux sources : table Suscription ET utilisateur.promotions
        java.util.Set<String> promotionIds = new java.util.LinkedHashSet<>();

        // Source 1 — table Suscription (bypassPromotion, module-suscription)
        souscriptionRepo.findAll().stream()
                .filter(s -> userId.equals(s.getUtilisateurId()) && s.getPromotionId() != null)
                .forEach(s -> promotionIds.add(s.getPromotionId()));

        // Source 2 — utilisateur.promotions (followPromotion, inscription directe)
        utilisateurRepo.findById(userId).ifPresent(u -> {
            if (u.getPromotions() != null) {
                u.getPromotions().stream()
                        .filter(p -> p.getId() != null)
                        .forEach(p -> promotionIds.add(p.getId()));
            }
        });

        // Récupérer les données fraîches depuis le repository
        List<Promotion> result = new ArrayList<>();
        for (String promoId : promotionIds) {
            promotionRepository.findById(promoId).ifPresent(result::add);
        }
        return result;
    }

    // ─── Modification ─────────────────────────────────────────────────────────

    public ResponseEntity<Promotion> updatePromotion(Promotion promotion) {
        try {
            promotion.setStatus(promotion.calculerStatut());
            return ResponseEntity.ok(promotionRepository.save(promotion));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    public ResponseEntity<Object> deletePromotion(String id) {
        try {
            promotionRepository.deleteById(id);
            return ResponseEntity.ok("Promotion supprimée");
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Assigner un formateur à un module spécifique dans la promotion
    public ResponseEntity<Object> assignerFormateurAModule(String promotionId, String moduleId, String formateurId) {
        try {
            Promotion promotion = promotionRepository.findById(promotionId).orElse(null);
            if (promotion == null)
                return ResponseEntity.badRequest().body("Promotion introuvable");
            Formateur formateur = formateurRepo.findById(formateurId).orElse(null);
            if (formateur == null)
                return ResponseEntity.badRequest().body("Formateur introuvable");

            boolean found = false;
            for (PromotionModule pm : promotion.getModules()) {
                if (pm.getModule() != null && pm.getModule().getId().equals(moduleId)) {
                    pm.setFormateur(formateur);
                    found = true;
                    break;
                }
            }
            if (!found)
                return ResponseEntity.badRequest().body("Module non trouvé dans cette promotion");

            promotionRepository.save(promotion);
            return ResponseEntity.ok("Formateur assigné avec succès");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    // ─── Liste d'attente ──────────────────────────────────────────────────────

    public ResponseEntity<Object> ajouterListeAttente(String promotionId, String etudiantId) {
        try {
            Promotion promotion = promotionRepository.findById(promotionId).orElse(null);
            if (promotion == null)
                return ResponseEntity.badRequest().body("Promotion introuvable");
            promotion.ajouterEnListeAttente(etudiantId);
            promotionRepository.save(promotion);
            return ResponseEntity.ok("Étudiant ajouté à la liste d'attente");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    public ResponseEntity<Object> retirerListeAttente(String promotionId, String etudiantId) {
        try {
            Promotion promotion = promotionRepository.findById(promotionId).orElse(null);
            if (promotion == null)
                return ResponseEntity.badRequest().body("Promotion introuvable");
            promotion.retirerDeListeAttente(etudiantId);
            promotionRepository.save(promotion);
            return ResponseEntity.ok("Étudiant retiré de la liste d'attente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // ─── Progression ──────────────────────────────────────────────────────────

    public double promotionProgression(String userId, String promotionId) {
        try {
            Promotion promotion = promotionRepository.findById(promotionId).orElse(null);
            if (promotion == null || promotion.getModules().isEmpty())
                return 0;

            int totalSeances = 0;
            int seancesSuivies = 0;
            for (PromotionModule pm : promotion.getModules()) {
                if (pm.getModule() == null)
                    continue;
                Modules module = modulesRepository.findById(pm.getModule().getId()).orElse(null);
                if (module == null)
                    continue;
                totalSeances += module.getSeances().size();
                for (var suivi : suiviCourRepository.findAll()) {
                    if (userId.equals(suivi.getUtilisateur()) && pm.getModule().getId().equals(suivi.getModule())) {
                        seancesSuivies++;
                    }
                }
            }
            if (totalSeances == 0 || seancesSuivies == 0)
                return 0;
            return (seancesSuivies * 100.0) / totalSeances;
        } catch (Exception e) {
            return 0;
        }
    }

    // ─── Scheduler — Mise à jour automatique des statuts ──────────────────────

    @Scheduled(cron = "0 0 * * * *") // toutes les heures
    public void updateAllStatuts() {
        try {
            List<Promotion> promotions = promotionRepository.findAll();
            for (Promotion p : promotions) {
                PromotionStatus nouveau = p.calculerStatut();
                if (nouveau != p.getStatus()) {
                    p.setStatus(nouveau);
                    promotionRepository.save(p);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ─── Helpers ──────────────────────────────────────────────────────────────

    public Groupe getGroupeByPromotionId(String id) {
        return groupeService.promotionGroupe(id);
    }

    public List<Etudiant> fetchAllStudentForPromotion(String promotionId) {
        try {
            List<Etudiant> etudiants = new ArrayList<>();
            for (Etudiant user : etudiantRepo.findAll()) {
                if (user.getPromotions() != null) {
                    for (var promo : user.getPromotions()) {
                        if (promo.getId().equals(promotionId)) {
                            etudiants.add(user);
                            break;
                        }
                    }
                }
            }
            return etudiants;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public String sendRappel(String promotionId) {
        try {
            Promotion promotion = promotionRepository.findById(promotionId).get();
            if (promotion == null) {
                throw new Exception("Promotion introuvable");
                // return "Promotion introuvable";
            }
            List<String> listeAttente = promotion.getListeAttente();
            List<Utilisateur> utilisateurs = utilisateurRepo.findAllById(listeAttente);
            List<String> emails = new ArrayList<>();
            emails.addAll(utilisateurs.stream().map(Utilisateur::getEmail).toList());
            if (utilisateurs.size() == 0) {
                List<Etudiant> etudiants = etudiantRepo.findAllById(listeAttente);
                emails.addAll(etudiants.stream().map(Etudiant::getEmail).toList());
            }
            if (emails.size() == 0) {
                return "Aucun étudiant en liste d'attente pour cette promotion";
            }
            BodyEmail email = new BodyEmail();

            email.setBody("Rappel : Nouvelle promotion ESSIKIA bientôt disponible !");
            email.setRecipient("support@essikia.com");

            String name = promotion.getName();
            String dateDebut = promotion.getDateDebut().format(DATE_FORMATTER);
            String dateFin = promotion.getDateFin().format(DATE_FORMATTER);
            // List<String> modules=promotion.getModules().stream()
            // .filter(pm->pm.getModule()!=null)
            // .map(pm->pm.getModule().getName())
            // .toList();

            String htmlContent = EmailTemplates.debutPromotion("",
                    name, dateDebut, dateFin, null, "", "");

            emailService.sendHtmlMailWithCc(email, htmlContent, emails);

            return "Rappel envoyé à " + emails.size() + " étudiants en liste d'attente";
        } catch (Exception e) {
            e.printStackTrace();
            return "Erreur lors de l'envoi du rappel : " + e.getMessage();
        }
    }

    // update promotion
    public void updatePromotion(Promotion promotion, String id) {

        try {
            Promotion existingPromotion = promotionRepository.findById(id).orElse(null);
            if (existingPromotion == null) {
                return;
            }
            existingPromotion.setName(promotion.getName());
            existingPromotion.setDescription(promotion.getDescription());
            existingPromotion.setDateInscription(promotion.getDateInscription());
            existingPromotion.setDateDebut(promotion.getDateDebut());
            existingPromotion.setDateFin(promotion.getDateFin());
            existingPromotion.setJoursAvantLancement(promotion.getJoursAvantLancement());
            promotionRepository.save(existingPromotion);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void assignPromotionModule(
            String promotionId,
            String moduleId,
            String formateurId) {
        try {
            Promotion promotion = promotionRepository.findById(promotionId).orElse(null);
            if (promotion == null)
                return;
            Modules module = modulesRepository.findById(moduleId).orElse(null);
            if (module == null)
                return;
            Formateur formateur = formateurRepo.findById(formateurId).orElse(null);
            if (formateur == null)
                return;

            List<PromotionModule> modules = promotion.getModules();
            for (var item : modules) {
                if (item.getModule().getId().equals(moduleId)) {
                    item.setFormateur(formateur);
                    break;
                }
            }
            String htmlContent = EmailTemplates.moduleAssigneFormateur(
                    formateur.getPrenom() + " " + formateur.getNom(),
                    module.getNom(),
                    promotion.getName(),
                    promotion.getDateDebut().format(DATE_FORMATTER),

                    null);
            BodyEmail email = new BodyEmail();
            email.setBody("Un nouveau module vous a été assigné !");
            email.setRecipient(formateur.getEmail());
            email.setMessage(htmlContent);
            emailService.sendHtlmlMail(email, htmlContent);
            promotionRepository.save(promotion);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
