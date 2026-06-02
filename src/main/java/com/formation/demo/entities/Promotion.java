package com.formation.demo.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import com.formation.demo.enumeration.PromotionStatus;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Document
@RequiredArgsConstructor
@Data
public class Promotion {

    private String id;
    private String name;
    private String description;

    // Chaque module de la promotion avec son formateur assigné
    private List<PromotionModule> modules = new ArrayList<>();

    // Dates clés
    private LocalDate dateInscription;     // date d'ouverture des inscriptions
    private int joursAvantLancement;       // délai entre dateInscription et dateDebut
    private LocalDate dateDebut;
    private LocalDate dateFin;

    // Statut calculé automatiquement
    private PromotionStatus status = PromotionStatus.A_VENIR;

    // Liste d'attente : IDs des étudiants qui veulent la prochaine promotion
    private List<String> listeAttente = new ArrayList<>();

    public void ajouterModule(PromotionModule pm) {
        this.modules.add(pm);
    }

    public void ajouterEnListeAttente(String etudiantId) {
        if (!this.listeAttente.contains(etudiantId)) {
            this.listeAttente.add(etudiantId);
        }
    }

    public void retirerDeListeAttente(String etudiantId) {
        this.listeAttente.remove(etudiantId);
    }

    // Calcul du statut en fonction des dates
    public PromotionStatus calculerStatut() {
        LocalDate today = LocalDate.now();
        if (dateInscription == null || dateDebut == null || dateFin == null) {
            return PromotionStatus.A_VENIR;
        }
        if (today.isBefore(dateInscription)) {
            return PromotionStatus.A_VENIR;
        }
        if (!today.isBefore(dateInscription) && today.isBefore(dateDebut)) {
            return PromotionStatus.INSCRIPTION_OUVERTE;
        }
        if (!today.isBefore(dateDebut) && !today.isAfter(dateFin)) {
            return PromotionStatus.EN_COURS;
        }
        return PromotionStatus.TERMINEE;
    }
}
