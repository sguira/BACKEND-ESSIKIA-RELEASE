package com.formation.demo.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.formation.demo.entities.Offre;
import com.formation.demo.repository.OffreRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OffreService {

    private final OffreRepo offreRepo;

    public List<Offre> getAllOffres() {
        return offreRepo.findAll();
    }

    public Offre getOffreById(String id) {
        return offreRepo.findById(id).orElse(null);
    }

    public Offre createOffre(Offre offre) {
        return offreRepo.save(offre);
    }

    public Offre updateOffre(String id, Offre offre) {
        Offre existingOffre = offreRepo.findById(id).orElse(null);
        if (existingOffre != null) {
            existingOffre.setNom(offre.getNom());
            existingOffre.setDescription(offre.getDescription());
            existingOffre.setTypeOffre(offre.getTypeOffre());
            existingOffre.setPrix(offre.getPrix());
            existingOffre.setNombrePaiements(offre.getNombrePaiements());
            existingOffre.setDureeEnMois(offre.getDureeEnMois());
            existingOffre.setActive(offre.isActive());
            existingOffre.setOptions(offre.getOptions());
            return offreRepo.save(existingOffre);
        }
        return null;
    }

    public void deleteOffre(String id) {
        offreRepo.deleteById(id);
    }

}
