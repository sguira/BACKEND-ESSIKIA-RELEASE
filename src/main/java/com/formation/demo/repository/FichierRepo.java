package com.formation.demo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.formation.demo.entities.Fichiers;

public interface FichierRepo extends MongoRepository<Fichiers, String> {

}
