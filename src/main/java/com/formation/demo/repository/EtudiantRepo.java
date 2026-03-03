package com.formation.demo.repository;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.formation.demo.entities.Etudiant;

// import lombok.Data;

public interface EtudiantRepo extends MongoRepository<Etudiant, String> {

    public Etudiant findByEmail(String email);

}
