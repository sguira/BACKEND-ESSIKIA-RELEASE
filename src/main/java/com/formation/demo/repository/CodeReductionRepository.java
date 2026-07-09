package com.formation.demo.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.formation.demo.entities.CodeReduction;

public interface CodeReductionRepository extends MongoRepository<CodeReduction, String> {
    Optional<CodeReduction> findByCodeIgnoreCase(String code);
}
