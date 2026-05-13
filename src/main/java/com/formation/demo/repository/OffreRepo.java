package com.formation.demo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.formation.demo.entities.Offre;
import com.formation.demo.enumeration.TypeOffre;

public interface OffreRepo extends MongoRepository<Offre, String> {
    Offre findByTypeOffre(TypeOffre typeOffre);
}
