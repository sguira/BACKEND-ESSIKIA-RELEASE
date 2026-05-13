package com.formation.demo.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.formation.demo.entities.VideoExplicative;

public interface VideoExplicativeRepository extends MongoRepository<VideoExplicative, String> {

    VideoExplicative findBySeanceId(String seanceId);

    Optional<VideoExplicative> findBySeanceIdAndPromotionId(String seanceId, String promotionId);

}