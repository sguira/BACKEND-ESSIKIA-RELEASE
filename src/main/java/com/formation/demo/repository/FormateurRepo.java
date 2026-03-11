package com.formation.demo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.formation.demo.entities.Formateur;

public interface FormateurRepo extends MongoRepository<Formateur, String> {

    public Formateur findByEmail(String id);

    public void deleteByEmail(String email);

}
