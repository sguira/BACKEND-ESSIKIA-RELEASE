package com.formation.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.formation.demo.entities.Quizz;

public interface QuizzRepository extends MongoRepository<Quizz, String> {

    List<Quizz> findByPromotionIdAndActiveTrue(String promotionId);

    Optional<Quizz> findBySeanceIdAndPromotionId(String seanceId, String promotionId);

    List<Quizz> findBySeanceId(String seanceId);

    boolean existsBySeanceId(String seanceId);

}
