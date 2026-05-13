package com.formation.demo.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.formation.demo.entities.Etudiant;
import com.formation.demo.entities.Formateur;
import com.formation.demo.entities.Matiere;
import com.formation.demo.repository.EtudiantRepo;
import com.formation.demo.repository.FormateurRepo;
import com.formation.demo.repository.MatiereRepo;

import lombok.RequiredArgsConstructor;

@Service
@Component
@RequiredArgsConstructor
public class MatiereService {

    private final MatiereRepo matiereRepo;
    private final EtudiantRepo etudiantRepo;
    private final FormateurRepo formateurRepo;

    public ResponseEntity<Matiere> createMatiere(Matiere matiere) {
        return ResponseEntity.ok(matiereRepo.save(matiere));
    }

    public ResponseEntity<Matiere> updateMatiere(Matiere matiere) {
        try {
            return ResponseEntity.ok(matiereRepo.save(matiere));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // liste des etudiant qui suivent une matiere
    public ResponseEntity<List<Etudiant>> getStudentByMatiere(String id) {
        try {
            List<Etudiant> etudiants = etudiantRepo.findAll();
            List<Etudiant> output = new ArrayList<>();
            for (Etudiant ele : etudiants) {
                ele.getMatieres().forEach((e) -> {
                    if (e.getId().equals(id)) {
                        output.add(ele);
                    }
                });
            }

            return ResponseEntity.ok(output);

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    public List<Matiere> allMatiere() {
        return matiereRepo.findAll();
    }

    public ResponseEntity<Object> assigner(String idMatiere, String idProf) {
        try {
            Formateur formateur = formateurRepo.findById(idProf).get();
            Matiere matiere = matiereRepo.findById(idMatiere).get();
            if (matiere != null) {
                if (matiere.getFormateur() == null) {
                    matiere.setFormateur(formateur);
                    return ResponseEntity.ok().body(matiereRepo.save(matiere));
                }
                return ResponseEntity.badRequest().body("Déja assigné à un form");
            }
            return ResponseEntity.badRequest().body("La matière n'existe pas ");
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

}
