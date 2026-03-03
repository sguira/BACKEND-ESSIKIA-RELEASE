package com.formation.demo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.formation.demo.entities.Note;

public interface NoteRepo extends MongoRepository<Note, String> {

}
