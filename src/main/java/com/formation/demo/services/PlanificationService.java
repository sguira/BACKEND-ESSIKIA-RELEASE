package com.formation.demo.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.formation.demo.email.BodyEmail;
import com.formation.demo.email.EmailServiceImp;
import com.formation.demo.entities.Groupe;
import com.formation.demo.entities.Message;
import com.formation.demo.entities.Planification;
import com.formation.demo.entities.Promotion;
import com.formation.demo.entities.Utilisateur;
import com.formation.demo.repository.PlanificationRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlanificationService {

    private final PlanificationRepo planificationRepo;
    private final EmailServiceImp emailService;
    private final FormateurService formateurService;
    private final GroupeService groupeService;
    private final PromotionService promotionService;
    private final MessageService messageService;
    private final UtilisateurService utilisateurService;

    // Méthodes de gestion des planifications peuvent être ajoutées ici
    @Transactional
    public Planification planificationSeance(Planification planification) {
        List<Utilisateur> admins = utilisateurService.getAllAdmin();

        // Envoi d'un email de notification à tous les administrateurs
        for (Utilisateur admin : admins) {
            BodyEmail bodyEmail = new BodyEmail();
            bodyEmail.setBody("Nouvelle planification de séance");
            bodyEmail.setMessage(generateHtmlMessage());
            bodyEmail.setRecipient(admin.getEmail());
            emailService.sendHtlmlMail(bodyEmail, generateHtmlMessage());
        }

        return planificationRepo.save(planification);
    }

    public String generateHtmlMessage() {
        return "<html>" +
                "<body>" +
                "<h1>Nouvelle planification de séance</h1>" +
                "<p>Une nouvelle séance a été planifiée. Veuillez consulter votre espace pour plus de détails.</p>" +
                "</body>" +
                "</html>";
    }

    // Liste des planifications pour un formateur spécifique
    public Iterable<Planification> getPlanificationsByFormateur(String formateurId) {
        return planificationRepo.findByFormateur(formateurId);
    }

    // suppression d'une planification par son ID
    @Transactional
    public void deletePlanificationById(String planificationId) {
        // if(!planificationRepo.existsById(planificationId)) {
        // throw new IllegalArgumentException("La planification avec l'ID spécifié
        // n'existe pas.");
        // }

        Planification planification = planificationRepo.findById(planificationId).orElse(null);
        if (planification != null) {
            planificationRepo.delete(planification);
        }

        try {
            Groupe groupe = promotionService.getgroupeBypromotionId(planification.getPromotionId());
            Message message = new Message();
            message.setContent("⚠️ Annulation de séance\n\n"
                    + "La séance initialement prévue le " + planification.getDateDebut() + " a été annulée.\n\n"
                    + "Vous serez informé(e) de la nouvelle date de reprogrammation si applicable.\n\n"
                    + "Nous nous excusons pour la gêne occasionnée.");
            message.setPromotion(planification.getPromotionId());
            message.setUser(planification.getFormateur());
            message.setGroupe(groupe.getId());
            messageService.createMessage(message);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            planificationRepo.deleteById(planification.getId());
        }
    }

    public Planification getPlanificationById(String id) {
        return planificationRepo.findById(id).orElse(null);
    }

}
