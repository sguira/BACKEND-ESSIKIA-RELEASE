package com.formation.demo.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.formation.demo.entities.Matiere;

public interface MatiereRepo extends MongoRepository<Matiere, String> {

    public List<Matiere> findByModuleId(String id);

}
