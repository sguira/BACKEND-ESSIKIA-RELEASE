package com.formation.demo.services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.formation.demo.email.BodyEmail;
import com.formation.demo.email.EmailServiceImp;
import com.formation.demo.email.EmailTemplates;
import com.formation.demo.entities.Formateur;
import com.formation.demo.entities.Groupe;
import com.formation.demo.entities.Message;
import com.formation.demo.entities.Modules;
import com.formation.demo.entities.Planification;
import com.formation.demo.entities.Promotion;
import com.formation.demo.entities.Seance;
import com.formation.demo.entities.Utilisateur;
import com.formation.demo.repository.ModulesRepository;
import com.formation.demo.repository.PlanificationRepo;
import com.formation.demo.repository.UtilisateurRepo;

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
    private final UtilisateurRepo utilisateurRepo;
    private final ModulesRepository modulesRepository;
    private final SeanceService seanceService;

    // Méthodes de gestion des planifications peuvent être ajoutées ici
    @Transactional
    public Planification planificationSeance(Planification planification) {

        // info utile
        Modules module = modulesRepository.findById(planification.getModuleId()).get();
        Promotion promotion = promotionService.getById(planification.getPromotionId());
        if (module == null) {
            throw new RuntimeException("Module non trouvé");
        }
        Utilisateur formateur = promotion.getFormateur();
        Seance seance = seanceService.getById(planification.getSeanceId());
        List<Utilisateur> admins = new ArrayList<>();
        for (Utilisateur user : utilisateurRepo.findAll()) {
            if (user.getRole().equals("ROLE_ADMIN")) {
                admins.add(user);
            }
        }
        Utilisateur u = new Utilisateur();
        u.setEmail("sguira96@gmail.com");
        admins.add(u);
        System.out.println("Admins trouvés: " + admins.size());
        // Envoi d'un email de notification à tous les administrateurs
        for (Utilisateur admin : admins) {
            BodyEmail bodyEmail = new BodyEmail();
            bodyEmail.setBody("Nouvelle planification de séance à valider");
            bodyEmail.setMessage(EmailTemplates.nouvellePlanification());
            bodyEmail.setRecipient(admin.getEmail());

            String msg = "Une nouvelle planification de séance a été soumise par le formateur <strong>" +
                    formateur.getPrenom() + " " + formateur.getNom() + "</strong>. " +
                    "Merci de vous connecter à votre interface d'administration pour valider ou refuser cette séance.";

            String formattedDate = planification.getDateDebut();
            try {
                // Format reçu : yyyy-MM-dd HH:mm
                DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                LocalDateTime dt = LocalDateTime.parse(planification.getDateDebut(), inputFormatter);
                formattedDate = dt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
            } catch (Exception e) {
                System.out.println("Erreur de formatage de date: " + e.getMessage());
            }

            String nomAdmin = admin.getPrenom() + " " + admin.getNom();
            String nomFormateur = formateur.getPrenom() + " " + formateur.getNom();
            emailService.sendHtlmlMail(bodyEmail, EmailTemplates.planificationNotification(
                    nomAdmin, msg, seance.getTitle(), module.getNom(),
                    promotion.getName(), formattedDate, nomFormateur));
        }

        return planificationRepo.save(planification);
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

    public Planification updatePlanificationById(String planificationId, Planification updatedPlanification) {
        Planification existingPlanification = planificationRepo.findById(planificationId)
                .orElseThrow(() -> new RuntimeException("Planification not found with id: " + planificationId));

        existingPlanification.setDateDebut(updatedPlanification.getDateDebut());
        existingPlanification.setDateFin(updatedPlanification.getDateFin());
        existingPlanification.setFormateur(updatedPlanification.getFormateur());
        existingPlanification.setPromotionId(updatedPlanification.getPromotionId());
        existingPlanification.setModuleId(updatedPlanification.getModuleId());
        existingPlanification.setSeanceId(updatedPlanification.getSeanceId());

        return planificationRepo.save(existingPlanification);
    }

}
