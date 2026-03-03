package com.formation.demo.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.formation.demo.entities.QuizSubmission;

import jakarta.annotation.Resource;

@Resource
public interface QuizSubmissionRepository
        extends MongoRepository<QuizSubmission, String> {

    Optional<QuizSubmission> findTopByQuizzIdAndUserIdOrderBySubmittedAtDesc(
            String quizzId,
            String userId);
}
