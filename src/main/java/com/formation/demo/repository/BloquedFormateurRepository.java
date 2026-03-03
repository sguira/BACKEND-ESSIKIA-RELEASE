package com.formation.demo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.formation.demo.entities.FormateurBloqued;

public interface BloquedFormateurRepository extends MongoRepository<FormateurBloqued, String> {

}
