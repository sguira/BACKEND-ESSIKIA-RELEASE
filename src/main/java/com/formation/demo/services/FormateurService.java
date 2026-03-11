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
import com.formation.demo.entities.Formateur;
import com.formation.demo.entities.FormateurBloqued;
import com.formation.demo.entities.Groupe;
import com.formation.demo.entities.Matiere;
import com.formation.demo.entities.Message;
import com.formation.demo.entities.Planification;
import com.formation.demo.entities.Promotion;
import com.formation.demo.entities.Utilisateur;
import com.formation.demo.enumeration.PlanificationStatus;
import com.formation.demo.repository.BloquedFormateurRepository;
import com.formation.demo.repository.FormateurRepo;
import com.formation.demo.repository.MessageRepository;
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

    public ResponseEntity<Object> listeFormateur() {
        try {

            return ResponseEntity.ok().body(formateurRepo.findAll());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // Liste des promotions assignées au formateur
    public List<Promotion> listesDesPromotions(String id) {
        try {
            List<Promotion> promotions = new ArrayList<>();

            for (var promotion : promotionRepo.findAll()) {
                if (promotion.getFormateur() != null && promotion.getFormateur().getId().equals(id)) {
                    promotions.add(promotion);
                    System.out.println(
                            "Promotion " + promotion.getName() + " added to the list for formateur with id: " + id);
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

    // bloquer un enseignant
    public void bloquerFormateur(BloqueFormateurDTO bloqueFormateurDTO) {

        Formateur formateur = formateurRepo.findByEmail(bloqueFormateurDTO.getEmail());
        Utilisateur utilisateur = formateurRepo.findByEmail(bloqueFormateurDTO.getEmail());
        if (formateur == null) {
            System.out.println("Formateur not found with email: " + bloqueFormateurDTO.getEmail());
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
            emailService.sendHtlmlMail(bodyEmail, generatedHtmlEmail(subject, message));
            System.out.println("Email sent successfully to " + formateur.getEmail());
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
            System.out.println("Formateur " + formateur.getEmail() + " has been blocked and saved to the database.");
        }
    }

    // debloquer un enseignant
    public void debloquerFormateur(BloqueFormateurDTO bloqueFormateurDTO) {
        Formateur formateur = formateurRepo.findByEmail(bloqueFormateurDTO.getEmail());
        Utilisateur utilisateur = formateurRepo.findByEmail(bloqueFormateurDTO.getEmail());
        if (formateur == null) {
            System.out.println("Formateur not found with email: " + bloqueFormateurDTO.getEmail());
            throw new RuntimeException("Formateur not found with email: " + bloqueFormateurDTO.getEmail());

        }
        try {
            String subject = "Notification de déblocage de compte";
            String message = "Cher " + formateur.getNom() + ",\n\n" +
                    "Nous vous informons que votre compte a été débloqué. Vous pouvez désormais accéder à votre compte et utiliser nos services normalement.\n\n"
                    +
                    "Si vous avez des questions ou des préoccupations, n'hésitez pas à contacter notre support pour plus d'informations.\n\n"
                    +
                    "Cordialement,\nL'équipe de support";
            BodyEmail bodyEmail = new BodyEmail();
            bodyEmail.setBody(subject);

            bodyEmail.setMessage(message);
            bodyEmail.setRecipient(formateur.getEmail());
            emailService.sendHtlmlMail(bodyEmail, generatedHtmlEmail(subject, message));
            System.out.println("Email sent successfully to " + formateur.getEmail());
        } catch (Exception e) {
            e.printStackTrace();
        }
        formateur.setBloqued(false);
        formateur.setBlocked(false);
        utilisateur.setBlocked(false);
        // utilisateur
        formateurRepo.save(formateur);
        utilisateurRepo.save(utilisateur);
        System.out.println("Formateur " + formateur.getEmail() + " has been unblocked and saved to the database.");
    }

    String generatedHtmlEmail(String subject, String message) {
        return "<html>" +
                "<body>" +
                "<h1>" + subject + "</h1>" +
                "<p>" + message + "</p>" +
                "</body>" +
                "</html>";
    }

    // envoyé rapport d'une séance
    public void envoyerRappel(SendRappel sendRappel) {

        if (sendRappel.getFormateurEmail() == null || sendRappel.getFormateurEmail().isEmpty()) {
            System.out.println("Formateur email is missing in the rappel request.");
            throw new RuntimeException("Formateur email is required to send a rappel.");
        }

        Planification planification = planificationRepo.findById(sendRappel.getPlanificationId()).orElse(null);
        if (planification == null) {
            System.out.println("Planification not found with id: " + sendRappel.getPlanificationId());
            throw new RuntimeException("Planification not found with id: " + sendRappel.getPlanificationId());
        }
        if (planification.getStatus() != PlanificationStatus.ACCEPTER) {
            System.out.println("Planification with id: " + sendRappel.getPlanificationId()
                    + " is not accepted. Current status: " + planification.getStatus());
            throw new RuntimeException(
                    "Only accepted planifications can be sent as rappel. Current status: " + planification.getStatus());
        }

        Formateur formateur = formateurRepo.findByEmail(sendRappel.getFormateurEmail());

        Groupe groupe = groupeService.promotionGroupe(planification.getPromotionId());
        Message message = new Message();
        message.setUser(formateur);
        message.setGroupe(groupe.getId());
        message.setPromotion(planification.getPromotionId());
        message.setContent("⏰ Rappel de séance\n\n"
                + "La séance prévue le " + planification.getDateDebut() + " approche à grands pas !\n\n" +
                "Assurez-vous d'être prêt(e) et de vérifier les détails de la séance dans votre espace formateur.\n\n"
                + "Lien de la séance :" + planification.getLink());

        messageRepository.save(message);

    }

    // public ResponseEntity<Object> listesDesMatiere(String id) {
    // try {
    // List<Matiere> matieres = new ArrayList<>();
    // for (var promotion : promotionRepo.findAll()) {
    // for (var matiere : promotion.getMatieres()) {
    // if (matiere.getFormateur() != null &&
    // matiere.getFormateur().getId().equals(id)) {
    // matieres.add(matiere);
    // }
    // }
    // }
    // return ResponseEntity.ok(matieres);
    // } catch (Exception e) {
    // e.printStackTrace();
    // return
    // ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    // }
    // }

}