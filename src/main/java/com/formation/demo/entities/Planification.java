package com.formation.demo.entities;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.formation.demo.enumeration.PlanificationStatus;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Document
@Data
@RequiredArgsConstructor
public class Planification {
    @Id
    String id;
    String title;
    String description;
    String dateDebut;
    String dateFin;
    String promotionId;
    String seanceId;
    String moduleId;
    String link;
    PlanificationStatus status = PlanificationStatus.EN_ATTENTE;
    boolean isDone;
    boolean isCancelled;
    boolean isStarted;
    boolean sendMail;
    boolean rappel;
    Instant createdAt = Instant.now();
    @DBRef
    Formateur formateur;
}
