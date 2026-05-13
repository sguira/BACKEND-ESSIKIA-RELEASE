package com.formation.demo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.formation.demo.entities.RequestSuscription;

public interface RequestSuscribeRepo extends MongoRepository<RequestSuscription, String> {

    RequestSuscription findByEtudiant();

}
