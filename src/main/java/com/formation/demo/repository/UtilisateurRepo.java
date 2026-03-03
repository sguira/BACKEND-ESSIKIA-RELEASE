package com.formation.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.formation.demo.entities.Utilisateur;

public interface UtilisateurRepo extends MongoRepository<Utilisateur, String> {

    public Optional<Utilisateur> findByEmail(String email);

    public List<Utilisateur> findByRole(String role);

}
