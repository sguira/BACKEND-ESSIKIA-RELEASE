package com.formation.demo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.formation.demo.entities.OffreOption;

public interface OffreOptionRepo extends MongoRepository<OffreOption, String> {
}
