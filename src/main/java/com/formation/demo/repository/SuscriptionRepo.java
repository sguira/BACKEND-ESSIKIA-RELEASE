package com.formation.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.formation.demo.entities.Suscription;

@Repository
public interface SuscriptionRepo extends MongoRepository<Suscription, String> {

    Optional<Suscription> findByUtilisateurIdAndModuleId(String utilisateurId, String moduleId);

    Optional<Suscription> findTopByUtilisateurIdOrderByStartDateDesc(String utilisateurId);

    // Souscription la plus récente d'un utilisateur, retrouvée par email
    Optional<Suscription> findTopByEmailOrderByStartDateDesc(String email);

    Optional<Suscription> findByStripeSubscriptionId(String stripeSubscriptionId);

    // Returns all subscriptions for a user, most recent first
    List<Suscription> findByUtilisateurIdOrderByStartDateDesc(String utilisateurId);
}
