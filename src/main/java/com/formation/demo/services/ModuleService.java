package com.formation.demo.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import com.formation.demo.entities.Etudiant;
import com.formation.demo.entities.Matiere;
import com.formation.demo.entities.Modules;
import com.formation.demo.entities.Seance;
import com.formation.demo.entities.Suscription;
import com.formation.demo.entities.Utilisateur;
import com.formation.demo.repository.EtudiantRepo;
import com.formation.demo.repository.MatiereRepo;
import com.formation.demo.repository.ModulesRepository;
import com.formation.demo.repository.SeanceRepository;
import com.formation.demo.repository.SuscriptionRepo;
import com.formation.demo.repository.UtilisateurRepo;

import lombok.RequiredArgsConstructor;

@Service
@Component
@RequiredArgsConstructor
public class ModuleService {

    private final ModulesRepository modulesRepository;
    private final EtudiantRepo etudiantRepo;
    private final MatiereRepo matiereRepo;
    private final SeanceRepository seanceRepository;
    private final SuscriptionRepo suscriptionRepo;
    private final UtilisateurRepo utilisateurRepo;

    public ResponseEntity<Object> createModule(Modules modules) {
        try {
            // Étape 1 : sauvegarde du module vide (pour avoir un ID)
            System.out.println("Nouveau Module:" + modules.toString());
            Modules module = new Modules();
            module.setNom(modules.getNom());
            module.setDescription(modules.getDescription());
            module.setNbSemaine(modules.getNbSemaine());
            module.setMontant(modules.getMontant());
            module.setCategorie(modules.getCategorie());
            module.setMiniature(modules.getMiniature());
            modulesRepository.save(module);

            // Étape 2 : création de la matière liée
            Matiere matiere = new Matiere();
            matiere.setDescription(module.getDescription());
            matiere.setNom("Matière " + module.getNom());
            matiere.setNbSeance(module.getNbSemaine());
            matiere.setModuleId(module.getId());
            List<Seance> seances = new ArrayList<>();
            matiere = matiereRepo.save(matiere);
            // Étape 3 : création et liaison des séances
            if (modules.getSeances() != null && !modules.getSeances().isEmpty()) {
                for (int i = 0; i < modules.getSeances().size(); i++) {
                    Seance s = modules.getSeances().get(i);
                    Seance seance = new Seance();
                    seance.setTitle(s.getTitle());
                    seance.setDescription(s.getDescription());
                    // seance.setModule(module); // lien bidirectionnel correct
                    seance.setFormation(matiere);
                    seance.setModule(module);
                    seances.add(seanceRepository.save(seance));

                }
            }

            // Étape 4 : sauvegarde finale

            module.setSeances(seances);

            return ResponseEntity.ok(module);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public ResponseEntity<List<Modules>> allModule() {
        try {
            return ResponseEntity.ok(modulesRepository.findAll());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    public ResponseEntity updateModule(Modules modules) {
        try {
            return ResponseEntity.ok(modulesRepository.save(modules));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    public ResponseEntity<Object> deleteModule(String id) {
        try {
            modulesRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    public ResponseEntity<Modules> getModuleById(String id) {
        try {
            return ResponseEntity.ok(modulesRepository.findById(id).orElse(null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    public ResponseEntity<Object> getModuleByStudentScribe(String id) {
        try {
            Etudiant e = etudiantRepo.findById(id).get();
            if (e != null) {
                return ResponseEntity.ok(e.getModules());
            }
            return null;
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // liste des matières pour un modules
    public ResponseEntity<Object> listeMatiereByModule(String id) {
        try {
            return ResponseEntity.ok().body(matiereRepo.findByModuleId(id));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    public ResponseEntity<Object> addSeanceToModule(String moduleId, Seance seance) {
        try {
            Modules module = modulesRepository.findById(moduleId).orElse(null);
            if (module == null) {
                return ResponseEntity.status(404).body("Module non trouvé");
            }
            // Lier la séance au module
            seance.setModule(module);
            Seance savedSeance = seanceRepository.save(seance);

            return ResponseEntity.ok(savedSeance);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body("Une erreur est survenue");
        }
    }

    // Liste des modules par catégorie
    public ResponseEntity<Object> listModuleByCategory(String categorie) {
        try {
            return ResponseEntity.ok(modulesRepository.findByCategorie(categorie));
        } catch (Exception e) {
            return ResponseEntity.status(400).build();
        }
    }

    // La liste de mes modules
    public ResponseEntity<Object> listeModuleByEtudiant(String etudiantId) {
        try {
            List<Modules> modules = new ArrayList<>();
            Utilisateur e = utilisateurRepo.findById(etudiantId).orElse(null);
            for (Suscription sousSuscription : suscriptionRepo.findAll()) {
                // System.out.println("Subscription Module ID: " +
                // sousSuscription.getModuleId());
                System.out.println("Etudiant id" + e.getId() + "==" + sousSuscription.getUtilisateurId());
                if (sousSuscription.getUtilisateurId().equals(e.getId())
                        && sousSuscription.getStatus().equals("active")) {
                    Modules mod = modulesRepository.findById(sousSuscription.getModuleId()).orElse(null);
                    if (mod != null) {
                        modules.add(mod);
                    }
                }

            }
            return ResponseEntity.ok(modules);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body("Une erreur est survenue");
        }
    }

    public List<Seance> getSeancesByModule(String moduleId) {
        List<Seance> seances = new ArrayList<>();
        for (Seance seance : seanceRepository.findAll()) {
            if (seance.getModule() != null) {
                if (seance.getModule().getId() != null && seance.getModule().getId().equals(moduleId)) {
                    seances.add(seance);
                }
            }

        }
        return seances;
    }

    public void deleteSeanceFromModule(String moduleId, String seanceId) {
        Modules module = modulesRepository.findById(moduleId).orElse(null);
        if (module != null) {
            Seance seance = seanceRepository.findById(seanceId).orElse(null);
            if (seance != null && seance.getModule() != null && seance.getModule().getId().equals(moduleId)) {
                module.getSeances().removeIf(s -> s.getId().equals(seanceId));
                modulesRepository.save(module);
                seanceRepository.deleteById(seanceId);
            }
        }
    }

}
