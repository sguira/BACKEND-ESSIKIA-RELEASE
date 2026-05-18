package com.formation.demo.services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.formation.demo.email.BodyEmail;
import com.formation.demo.email.EmailServiceImp;
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
            bodyEmail.setMessage(generateHtmlMessage());
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

            emailService.sendHtlmlMail(bodyEmail, creerHtmlBody(formateur, admin, msg,
                    seance.getTitle(), formattedDate,
                    formateur.getPrenom() + " " + formateur.getNom(), module.getNom(), promotion.getName()));
        }

        return planificationRepo.save(planification);
    }

    public String creerHtmlBody(Utilisateur formateur, Utilisateur admin, String message, String nomSeance, String date,
            String nomFormateur, String nomModule, String nomPromotion) {
        String displayName = "";
        String adminname = "Admin";
        if (formateur != null) {
            String nom = formateur.getNom() != null ? formateur.getNom() : "";
            String prenom = formateur.getPrenom() != null ? formateur.getPrenom() : "";
            displayName = (prenom + " " + nom).trim();
        }
        if (admin != null) {
            String nom = admin.getNom() != null ? admin.getNom() : "";
            String prenom = admin.getPrenom() != null ? admin.getPrenom() : "";
            adminname = (prenom + " " + nom).trim();
        }
        if (displayName.isEmpty()) {
            displayName = "Formateur";
        }
        if (adminname.isEmpty()) {
            adminname = "Admin";
        }

        return "<!DOCTYPE html>" +
                "<html lang='fr'>" +
                "<head>" +
                "<meta charset='UTF-8'>" +
                "<meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                "<title>Essikia - Planification de séance</title>" +
                "</head>" +
                "<body style='margin:0;padding:0;background-color:#f4f7f9;font-family:\"Segoe UI\",Tahoma,Geneva,Verdana,sans-serif;color:#2d3748;'>"
                +
                "    <div style='max-width:600px;margin:30px auto;padding:0 20px;'>" +
                "        <div style='background: linear-gradient(135deg, #0f6d6d 0%, #1a4a4a 100%);color:#ffffff;padding:45px 30px;border-radius:24px 24px 0 0;text-align:center;box-shadow: 0 10px 25px -5px rgba(15, 109, 109, 0.2);'>"
                +
                "            <h1 style='margin:0;font-size:32px;font-weight:800;letter-spacing:-0.5px;'>Essikia Formation</h1>"
                +
                "            <p style='margin:12px 0 0;font-size:16px;opacity:0.85;font-weight:400;'>Portail d'Administration</p>"
                +
                "        </div>" +
                "        <div style='background:#ffffff;padding:45px 40px;border-radius:0 0 24px 24px;box-shadow:0 20px 25px -5px rgba(0,0,0,0.05), 0 10px 10px -5px rgba(0,0,0,0.02);'>"
                +
                "            <p style='margin:0 0 25px;font-size:18px;font-weight:700;color:#1a202c;'>Bonjour "
                + adminname + ",</p>" +
                "            <p style='margin:0 0 30px;font-size:16px;line-height:1.7;color:#4a5568;'>" +
                "                " + message + "" +
                "            </p>" +
                "            <div style='background-color:#f8fafc;border:1px solid #edf2f7;border-radius:20px;padding:35px;margin:35px 0;'>"
                +
                "                <h3 style='margin:0 0 25px 0;font-size:15px;text-transform:uppercase;letter-spacing:1.5px;color:#0f6d6d;font-weight:800;border-bottom:2px solid #0f6d6d;display:inline-block;padding-bottom:5px;'>Détails de la planification</h3>"
                +
                "                <table style='width:100%;border-collapse:collapse;'>" +
                "                    <tr>" +
                "                        <td style='padding:12px 0;color:#718096;font-size:14px;width:110px;font-weight:600;'>Séance</td>"
                +
                "                        <td style='padding:12px 0;color:#1a202c;font-size:16px;font-weight:700;'>"
                + nomSeance + "</td>" +
                "                    </tr>" +
                "                    <tr>" +
                "                        <td style='padding:12px 0;color:#718096;font-size:14px;font-weight:600;'>Module</td>"
                +
                "                        <td style='padding:12px 0;color:#1a202c;font-size:16px;font-weight:700;'>"
                + nomModule + "</td>" +
                "                    </tr>" +
                "                    <tr>" +
                "                        <td style='padding:12px 0;color:#718096;font-size:14px;font-weight:600;'>Promotion</td>"
                +
                "                        <td style='padding:12px 0;color:#1a202c;font-size:16px;font-weight:700;'>"
                + nomPromotion + "</td>" +
                "                    </tr>" +
                "                    <tr>" +
                "                        <td style='padding:12px 0;color:#718096;font-size:14px;font-weight:600;'>Date</td>"
                +
                "                        <td style='padding:12px 0;color:#1a202c;font-size:16px;font-weight:700;'>"
                + date + "</td>" +
                "                    </tr>" +
                "                    <tr>" +
                "                        <td style='padding:12px 0;color:#718096;font-size:14px;font-weight:600;'>Formateur</td>"
                +
                "                        <td style='padding:12px 0;color:#1a202c;font-size:16px;font-weight:700;'>"
                + nomFormateur + "</td>" +
                "                    </tr>" +
                "                </table>" +
                "            </div>" +
                "            <div style='text-align:center;margin-top:45px;'>" +
                "                <a href='#' style='background-color:#0f6d6d;color:#ffffff;padding:18px 35px;border-radius:14px;text-decoration:none;font-weight:700;font-size:16px;display:inline-block;box-shadow:0 10px 15px -3px rgba(15, 109, 109, 0.3);'>Gérer la planification</a>"
                +
                "            </div>" +
                "            <div style='margin-top:50px;padding-top:30px;border-top:1px solid #edf2f7;text-align:center;'>"
                +
                "                <p style='margin:0;font-size:13px;color:#a0aec0;'>Besoin d'aide ? Répondez simplement à cet email pour contacter notre support.</p>"
                +
                "            </div>" +
                "        </div>" +
                "        <div style='text-align:center;margin-top:30px;color:#a0aec0;font-size:12px;line-height:1.5;'>"
                +
                "            © 2026 Essikia Formation. Tous droits réservés.<br>" +
                "            Accompagner votre réussite, une séance à la fois." +
                "        </div>" +
                "    </div>" +
                "</body>" +
                "</html>";
    }

    public String creerHtmlBodyValidation(Utilisateur user, String nomSeance, String date) {
        String displayName = "";
        if (user != null) {
            String nom = user.getNom() != null ? user.getNom() : "";
            String prenom = user.getPrenom() != null ? user.getPrenom() : "";
            displayName = (prenom + " " + nom).trim();
        }
        if (displayName.isEmpty()) {
            displayName = "Formateur";
        }

        return "<!DOCTYPE html>" +
                "<html lang='fr'>" +
                "<head>" +
                "<meta charset='UTF-8'>" +
                "<meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                "<title>Séance Validée - Essikia</title>" +
                "</head>" +
                "<body style='margin:0;padding:0;background-color:#f4f7f9;font-family:\"Segoe UI\",Tahoma,Geneva,Verdana,sans-serif;color:#2d3748;'>" +
                "    <div style='max-width:600px;margin:40px auto;padding:0 20px;'>" +
                "        <div style='background:#ffffff;padding:40px;border-radius:16px;box-shadow:0 4px 12px rgba(0,0,0,0.08);border-top:5px solid #0f6d6d;'>" +
                "            <div style='text-align:center;margin-bottom:30px;'>" +
                "                <h2 style='color:#0f6d6d;margin:0;font-size:24px;'>Planification Validée</h2>" +
                "            </div>" +
                "            <p style='margin:0 0 20px;font-size:16px;'>Bonjour <strong>" + displayName + "</strong>,</p>" +
                "            <p style='margin:0 0 20px;font-size:16px;line-height:1.6;color:#4a5568;'>" +
                "                Nous vous informons que votre planification pour la séance <strong>" + nomSeance + "</strong> " +
                "                prévue le <strong>" + date + "</strong> a été <span style='color:#0f6d6d;font-weight:700;'>validée</span> par l'administration." +
                "            </p>" +
                "            <p style='margin:0 0 30px;font-size:16px;color:#4a5568;'>Elle est désormais visible par les étudiants sur la plateforme.</p>" +
                "            <div style='text-align:center;'>" +
                "                <a href='#' style='background-color:#0f6d6d;color:#ffffff;padding:14px 28px;border-radius:8px;text-decoration:none;font-weight:700;display:inline-block;'>Voir mon planning</a>" +
                "            </div>" +
                "        </div>" +
                "        <div style='text-align:center;margin-top:20px;color:#a0aec0;font-size:12px;'>" +
                "            © 2026 Essikia Formation" +
                "        </div>" +
                "    </div>" +
                "</body>" +
                "</html>";
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
