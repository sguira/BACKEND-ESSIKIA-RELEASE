package com.formation.demo.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.formation.demo.dto.BloqueFormateurDTO;
import com.formation.demo.dto.SendRappel;
import com.formation.demo.email.BodyEmail;
import com.formation.demo.email.EmailServiceImp;
import com.formation.demo.email.EmailTemplates;
import com.formation.demo.entities.Etudiant;
import com.formation.demo.entities.Formateur;
import com.formation.demo.entities.FormateurBloqued;
import com.formation.demo.entities.Groupe;
import com.formation.demo.entities.Message;
import com.formation.demo.entities.Modules;
import com.formation.demo.entities.Planification;
import com.formation.demo.entities.Promotion;
import com.formation.demo.entities.Seance;
import com.formation.demo.entities.Utilisateur;
import com.formation.demo.enumeration.PlanificationStatus;
import com.formation.demo.repository.BloquedFormateurRepository;
import com.formation.demo.repository.FormateurRepo;
import com.formation.demo.repository.MessageRepository;
import com.formation.demo.repository.ModulesRepository;
import com.formation.demo.repository.PlanificationRepo;
import com.formation.demo.repository.PromotionRepository;
import com.formation.demo.repository.UtilisateurRepo;

import lombok.RequiredArgsConstructor;

@Service
@Component
@RequiredArgsConstructor
public class FormateurService {

    private final FormateurRepo formateurRepo;
    private final PromotionRepository promotionRepo;
    private final GroupeService groupeService;
    private final EmailServiceImp emailService;
    private final BloquedFormateurRepository bloquedFormateurRepository;
    private final UtilisateurRepo utilisateurRepo;
    private final PlanificationRepo planificationRepo;
    private final MessageRepository messageRepository;
    private final ModulesRepository modulesRepository;
    private final SeanceService seanceService;

    public ResponseEntity<Object> listeFormateur() {
        try {
            return ResponseEntity.ok().body(formateurRepo.findAll());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    public List<Promotion> listesDesPromotions(String id) {
        try {
            List<Promotion> promotions = new ArrayList<>();
            for (var promotion : promotionRepo.findAll()) {
                if (promotion.getFormateur() != null && promotion.getFormateur().getId().equals(id)) {
                    promotions.add(promotion);
                }
            }
            return promotions;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<Groupe> listesDesGroupes(String formateurId) {
        try {
            List<Promotion> promotions = listesDesPromotions(formateurId);
            List<Groupe> groupes = new ArrayList<>();
            for (var promotion : promotions) {
                Groupe groupe = groupeService.promotionGroupe(promotion.getId());
                groupes.add(groupe);
            }
            return groupes;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void bloquerFormateur(BloqueFormateurDTO bloqueFormateurDTO) {
        Formateur formateur = formateurRepo.findByEmail(bloqueFormateurDTO.getEmail());
        Utilisateur utilisateur = formateurRepo.findByEmail(bloqueFormateurDTO.getEmail());
        if (formateur == null) {
            throw new RuntimeException("Formateur not found with email: " + bloqueFormateurDTO.getEmail());
        }
        String subject = "Notification de blocage de compte";
        String message = "Cher " + formateur.getNom() + ",\n\n" +
                "Nous vous informons que votre compte a été bloqué en raison de violations de nos politiques. plus précisement : "
                + bloqueFormateurDTO.getMotif() + "\n\n" +
                "Si vous pensez que cela est une erreur, veuillez contacter notre support pour plus d'informations.\n\n"
                +
                "Cordialement,\nL'équipe de support";
        try {
            BodyEmail bodyEmail = new BodyEmail();
            bodyEmail.setBody(subject);
            bodyEmail.setMessage(message);
            bodyEmail.setRecipient(formateur.getEmail());
            emailService.sendHtlmlMail(bodyEmail, EmailTemplates.gestionCompte(subject, message));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            formateur.setBloqued(true);
            formateur.setBlocked(true);
            utilisateur.setBlocked(true);
            formateurRepo.save(formateur);
            utilisateurRepo.save(utilisateur);
            FormateurBloqued formateurBloqued = FormateurBloqued.builder()
                    .formateur(formateur)
                    .motif(bloqueFormateurDTO.getMotif())
                    .build();
            bloquedFormateurRepository.save(formateurBloqued);
        }
    }

    public void debloquerFormateur(BloqueFormateurDTO bloqueFormateurDTO) {
        Formateur formateur = formateurRepo.findByEmail(bloqueFormateurDTO.getEmail());
        Utilisateur utilisateur = formateurRepo.findByEmail(bloqueFormateurDTO.getEmail());
        if (formateur == null) {
            throw new RuntimeException("Formateur not found with email: " + bloqueFormateurDTO.getEmail());
        }
        try {
            String subject = "Notification de déblocage de compte";
            String message = "Cher " + formateur.getNom() + ",\n\n" +
                    "Nous vous informons que votre compte a été débloqué. Vous pouvez désormais accéder à votre compte et utiliser nos services normalement.\n\n"
                    +
                    "Cordialement,\nL'équipe de support";
            BodyEmail bodyEmail = new BodyEmail();
            bodyEmail.setBody(subject);
            bodyEmail.setMessage(message);
            bodyEmail.setRecipient(formateur.getEmail());
            emailService.sendHtlmlMail(bodyEmail, EmailTemplates.gestionCompte(subject, message));
        } catch (Exception e) {
            e.printStackTrace();
        }
        formateur.setBloqued(false);
        formateur.setBlocked(false);
        utilisateur.setBlocked(false);
        formateurRepo.save(formateur);
        utilisateurRepo.save(utilisateur);
    }

    // Envoi Email 9 (rappel formateur) + Email 4 (rappel étudiants) + message chat
    public void envoyerRappel(SendRappel sendRappel) {
        if (sendRappel.getFormateurEmail() == null || sendRappel.getFormateurEmail().isEmpty()) {
            throw new RuntimeException("Formateur email is required to send a rappel.");
        }

        Planification planification = planificationRepo.findById(sendRappel.getPlanificationId()).orElse(null);
        if (planification == null) {
            throw new RuntimeException("Planification not found with id: " + sendRappel.getPlanificationId());
        }
        if (planification.getStatus() != PlanificationStatus.ACCEPTER) {
            throw new RuntimeException(
                    "Only accepted planifications can be sent as rappel. Current status: " + planification.getStatus());
        }

        Formateur formateur = formateurRepo.findByEmail(sendRappel.getFormateurEmail());
        Groupe groupe = groupeService.promotionGroupe(planification.getPromotionId());

        // Message chat au groupe
        Message message = new Message();
        message.setUser(formateur);
        message.setGroupe(groupe.getId());
        message.setPromotion(planification.getPromotionId());
        message.setContent("⏰ Rappel de séance\n\n"
                + "La séance prévue le " + planification.getDateDebut() + " approche à grands pas !\n\n"
                + "Assurez-vous d'être prêt(e) et de vérifier les détails de la séance dans votre espace.\n\n"
                + "Lien de la séance : " + planification.getLink());
        messageRepository.save(message);

        // Données communes pour les emails
        Modules module = modulesRepository.findById(planification.getModuleId()).orElse(null);
        Seance seance = seanceService.getById(planification.getSeanceId());
        Promotion promotion = promotionRepo.findById(planification.getPromotionId()).orElse(null);

        String nomModule = module != null ? module.getNom() : "";
        String titreSeance = seance != null ? seance.getTitle() : "";
        String heureDebut = planification.getDateDebut() != null ? planification.getDateDebut() : "";
        String heureFin = planification.getDateFin() != null ? planification.getDateFin() : "";
        String format = (planification.getLink() != null && !planification.getLink().isEmpty())
                ? "Visioconférence" : "Présentiel";
        int nbApprenants = (promotion != null && promotion.getEtudiants() != null)
                ? promotion.getEtudiants().size() : 0;

        // Email 9 — Rappel au formateur
        try {
            if (formateur != null) {
                BodyEmail formateurEmail = new BodyEmail();
                formateurEmail.setRecipient(formateur.getEmail());
                formateurEmail.setBody("Rappel — Vous animez une séance demain · ESSIKIA");
                String prenomFormateur = formateur.getPrenom() != null ? formateur.getPrenom() : formateur.getNom();
                emailService.sendHtlmlMail(formateurEmail,
                        EmailTemplates.rappelSeanceFormateur(prenomFormateur, nomModule, titreSeance,
                                heureDebut, heureDebut, heureFin, format, nbApprenants,
                                "https://essikia.fr/formateur"));
            }
        } catch (Exception e) {
            System.out.println("Erreur envoi email rappel formateur: " + e.getMessage());
        }

        // Email 4 — Rappel aux étudiants de la promotion
        if (promotion != null && promotion.getEtudiants() != null) {
            String lien = planification.getLink() != null ? planification.getLink() : "";
            for (Etudiant etudiant : promotion.getEtudiants()) {
                try {
                    BodyEmail etudiantEmail = new BodyEmail();
                    etudiantEmail.setRecipient(etudiant.getEmail());
                    etudiantEmail.setBody("Rappel — Ta séance ESSIKIA commence demain");
                    String prenomEtudiant = etudiant.getPrenom() != null ? etudiant.getPrenom() : etudiant.getNom();
                    emailService.sendHtlmlMail(etudiantEmail,
                            EmailTemplates.rappelSeanceEtudiant(prenomEtudiant, nomModule, titreSeance,
                                    heureDebut, heureDebut, heureFin, format, lien));
                } catch (Exception e) {
                    System.out.println("Erreur envoi email rappel étudiant " + etudiant.getEmail() + ": " + e.getMessage());
                }
            }
        }
    }
}
