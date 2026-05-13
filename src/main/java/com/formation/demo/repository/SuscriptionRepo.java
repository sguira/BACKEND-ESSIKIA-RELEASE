package com.formation.demo.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.formation.demo.entities.Suscription;

@Repository
public interface SuscriptionRepo extends MongoRepository<Suscription, String> {

    Optional<Suscription> findByUtilisateurIdAndModuleId(String utilisateurId, String moduleId);

    Optional<Suscription> findTopByUtilisateurIdOrderByStartDateDesc(String utilisateurId);

    Optional<Suscription> findByStripeSubscriptionId(String stripeSubscriptionId);
}
