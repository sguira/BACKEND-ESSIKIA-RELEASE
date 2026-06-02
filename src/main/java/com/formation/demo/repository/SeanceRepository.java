package com.formation.demo.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.formation.demo.entities.Seance;

public interface SeanceRepository extends MongoRepository<Seance, String> {

    // ObjectId obligatoire : MongoDB stocke les DBRef.$id en ObjectId, pas en
    // String
    @Query("{'module.$id': ?0}")
    List<Seance> findByModuleId(String moduleId);

    @Query("{'formation.$id': ?0}")
    List<Seance> findByFormationId(ObjectId formationId);

}
