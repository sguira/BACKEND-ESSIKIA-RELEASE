package com.formation.demo.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.formation.demo.entities.Matiere;
import com.formation.demo.entities.Seance;

public interface SeanceRepository extends MongoRepository<Seance, String> {

    // Custom query to find Seance by Matiere ID
    // List<Seance> findByFormation(Matiere matiere);
    List<Seance> findByModuleId(String moduleId);

}
