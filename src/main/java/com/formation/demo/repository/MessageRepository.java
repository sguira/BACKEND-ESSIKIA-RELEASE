package com.formation.demo.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.formation.demo.entities.Message;

public interface MessageRepository extends MongoRepository<Message, String> {

    public List<Message> findByPromotion(String id);

    public List<Message> findByGroupe(String id);
}
