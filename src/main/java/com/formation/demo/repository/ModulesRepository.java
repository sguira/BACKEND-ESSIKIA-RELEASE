package com.formation.demo.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.formation.demo.entities.Modules;

public interface ModulesRepository extends MongoRepository<Modules, String> {

    public List<Modules> findByCategorie(String categorie);
}
