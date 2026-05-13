package com.formation.demo.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.formation.demo.entities.SuiviCours;

import jakarta.annotation.Resource;

@Resource
public interface SuiviCourRepository extends MongoRepository<SuiviCours, String> {

    List<SuiviCours> findByUtilisateurIdAndIsCompletedTrue(String utilisateurId);

    List<SuiviCours> findByUtilisateurIdAndModuleIdAndIsCompletedTrue(String utilisateurId, String moduleId);

    boolean existsByUtilisateurIdAndModuleIdAndCurrentSeanceId(String utilisateurId, String moduleId, String seanceId);

    SuiviCours findByUtilisateurIdAndModuleIdAndCurrentSeanceId(String utilisateurId, String moduleId, String seanceId);

}
