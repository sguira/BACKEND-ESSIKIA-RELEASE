package com.formation.demo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.formation.demo.entities.SuiviCours;

import jakarta.annotation.Resource;

@Resource
public interface SuiviCourRepository extends MongoRepository<SuiviCours, String> {

    java.util.List<SuiviCours> findByUtilisateurIdAndIsCompletedTrue(String utilisateurId);

    java.util.List<SuiviCours> findByUtilisateurIdAndModuleIdAndIsCompletedTrue(String utilisateurId, String moduleId);

}
