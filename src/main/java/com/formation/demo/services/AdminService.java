package com.formation.demo.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.formation.demo.dto.ValidationPlanification;
import com.formation.demo.entities.Etudiant;
import com.formation.demo.entities.Groupe;
import com.formation.demo.entities.Message;
import com.formation.demo.entities.Planification;
import com.formation.demo.enumeration.PlanificationStatus;
import com.formation.demo.repository.EtudiantRepo;
import com.formation.demo.repository.MessageRepository;
import com.formation.demo.repository.PlanificationRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final PlanificationService planificationService;
    private final PlanificationRepo planificationRepo;
    private final GroupeService groupeService;
    private final MessageRepository messageRepository;
    private final EtudiantRepo etudiantRepo;

    public Planification validationPlanification(ValidationPlanification validationPlanification) {
        Planification planification = planificationService
                .getPlanificationById(validationPlanification.getPlanificationId());
        if (planification != null) {
            planification.setStatus(PlanificationStatus.ACCEPTER);
            planification.setLink(validationPlanification.getLink());
            Groupe groupe = groupeService.promotionGroupe(planification.getPromotionId());
            Message message = new Message();
            message.setContent("📅 Nouvelle séance planifiée\n\n"
                    + "📆 Date et heure : "
                    + (planification.getDateDebut() != null ? planification.getDateDebut() : "Non précisée") + "\n"
                    + "🔗 Lien de connexion : " + planification.getLink() + "\n\n"
                    + "Merci de vous connecter à l'heure prévue. En cas d'absence, veuillez prévenir à l'avance.\n\n"
                    + "Bonne séance !");
            message.setGroupe(groupe.getId());
            message.setIdModule(planification.getModuleId());
            message.setUser(planification.getFormateur());
            message.setPromotion(planification.getPromotionId());
            messageRepository.save(message);
            System.out.println(
                    "Message envoyé au groupe " + groupe.getName() + " pour la planification " + planification.getId());
            return planificationRepo.save(planification);
        } else {
            throw new RuntimeException(
                    "Planification not found with id: " + validationPlanification.getPlanificationId());
        }
    }

    public List<Planification> getAllPlanifications() {
        return planificationRepo.findAll();
    }

    public List<Planification> getPlanificationsByStatus(PlanificationStatus status) {
        try {
            return planificationRepo.findByStatus(status);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error retrieving planifications by status: " + e.getMessage());
        }
    }

    public Planification anuulerPlanification(String planificationId) {
        Planification planification = planificationRepo.findById(planificationId)
                .orElseThrow(() -> new RuntimeException("Planification not found with id: " + planificationId));
        planification.setStatus(PlanificationStatus.REFUSER);
        planificationRepo.save(planification);
        try {
            Groupe groupe = groupeService.promotionGroupe(planification.getPromotionId());
            Message message = new Message();
            message.setContent("⚠️ Annulation de séance\n\n"
                    + "La séance initialement prévue le " + planification.getDateDebut() + " a été annulée.\n\n"
                    + "Vous serez informé(e) de la nouvelle date de reprogrammation si applicable.\n\n"
                    + "Nous nous excusons pour la gêne occasionnée.");
            message.setPromotion(planification.getPromotionId());
            message.setUser(planification.getFormateur());
            message.setGroupe(groupe.getId());
            messageRepository.save(message);
            System.out.println("Message d'annulation envoyé au groupe " + groupe.getName() + " pour la planification "
                    + planification.getId());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            planificationRepo.save(planification);
        }
        return planification;
    }

    public Etudiant bloquerEtudiant(String email) {
        Etudiant etudiant = etudiantRepo.findByEmail(email);
        if (etudiant != null) {
            etudiant.setBlocked(true);
            return etudiantRepo.save(etudiant);
        } else {
            throw new RuntimeException("Étudiant non trouvé avec l'email : " + email);
        }
    }

    public Etudiant debloquerEtudiant(String email) {
        Etudiant etudiant = etudiantRepo.findByEmail(email);
        if (etudiant != null) {
            etudiant.setBlocked(false);
            return etudiantRepo.save(etudiant);
        } else {
            throw new RuntimeException("Étudiant non trouvé avec l'email : " + email);
        }
    }
}
