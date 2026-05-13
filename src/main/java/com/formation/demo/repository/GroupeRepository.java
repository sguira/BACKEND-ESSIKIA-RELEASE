package com.formation.demo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.formation.demo.entities.Groupe;
import com.formation.demo.entities.Promotion;

public interface GroupeRepository extends MongoRepository<Groupe, String> {

    public Groupe findGroupeByPromotion(Promotion promotion);

}
