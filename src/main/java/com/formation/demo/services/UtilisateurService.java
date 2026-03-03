package com.formation.demo.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.formation.demo.dto.UpdateUserDTO;
import com.formation.demo.entities.Etudiant;
import com.formation.demo.entities.Fichiers;
import com.formation.demo.entities.Utilisateur;
import com.formation.demo.repository.EtudiantRepo;
import com.formation.demo.repository.UtilisateurRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UtilisateurService {

    private final UtilisateurRepo utilisateurRepository;
    private final EtudiantRepo etudiantRepo;

    public void updateAvatar(String userId, Fichiers newAvatar) {
        Utilisateur utilisateur = utilisateurRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'ID: " + userId));

        utilisateur.ajouterAvatar(newAvatar);
        utilisateurRepository.save(utilisateur);
        System.out.println("Avatar ajouté pour l'utilisateur ID: " + userId);
    }

    public void updateUser(UpdateUserDTO user, String email) {
        Etudiant etudiant = etudiantRepo.findByEmail(email);

        Utilisateur utilisateur = utilisateurRepository.findByEmail(email).orElse(null);

        // Logique pour mettre à jour les informations de l'utilisateur
        // Par exemple, si 'user' est une instance de 'Utilisateur', vous pouvez faire :

        utilisateur.setNom(user.getNom());

        utilisateur.setPrenom(user.getPrenom());
        utilisateur.setProfession(user.getProfession());
        utilisateur.setTelephone(user.getTelephone());
        utilisateur.setAdresse(user.getAdresse());
        utilisateur.setObjectif(user.getObjectif());
        etudiant.setAdresse(user.getAdresse());
        etudiant.setProfession(user.getProfession());
        etudiant.setObjectif(user.getObjectif());
        etudiantRepo.save(etudiant);
        System.out.println("Informations de l'étudiant mises à jour pour l'utilisateur ID: " + email);
        // Mettez à jour d'autres champs selon vos besoins
        utilisateurRepository.save(utilisateur);
        System.out.println("Utilisateur mis à jour pour l'utilisateur ID: " + email);

    }

    public List<Utilisateur> getAllAdmin() {
        return utilisateurRepository.findByRole("ADMIN");
    }

}
