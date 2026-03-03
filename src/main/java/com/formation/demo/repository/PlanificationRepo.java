package com.formation.demo.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.formation.demo.entities.Planification;
import com.formation.demo.enumeration.PlanificationStatus;

import jakarta.annotation.Resource;

@Resource
public interface PlanificationRepo extends MongoRepository<Planification, String> {

    Iterable<Planification> findByFormateur(String formateurId);

    List<Planification> findByStatus(PlanificationStatus status);

    // existe une planification par son ID
    // boolean existsById(String planificationId);

}
