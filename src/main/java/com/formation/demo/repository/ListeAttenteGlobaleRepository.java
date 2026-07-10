package com.formation.demo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.formation.demo.entities.ListeAttenteGlobale;

public interface ListeAttenteGlobaleRepository extends MongoRepository<ListeAttenteGlobale, String> {

    boolean existsByEtudiantId(String etudiantId);

    void deleteByEtudiantId(String etudiantId);
}
