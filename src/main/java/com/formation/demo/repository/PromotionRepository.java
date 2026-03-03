package com.formation.demo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.formation.demo.entities.Promotion;

public interface PromotionRepository extends MongoRepository<Promotion, String> {

}
