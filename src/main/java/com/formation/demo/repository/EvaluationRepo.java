package com.formation.demo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.formation.demo.entities.Evaluation;

public interface EvaluationRepo extends MongoRepository<Evaluation, String> {

}
