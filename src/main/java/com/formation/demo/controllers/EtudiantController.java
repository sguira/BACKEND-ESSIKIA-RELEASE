package com.formation.demo.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.formation.demo.dto.CompleteSeanceModel;
import com.formation.demo.dto.UserFollowPromotionItem;
import com.formation.demo.entities.Etudiant;
import com.formation.demo.entities.Matiere;

import com.formation.demo.services.EtudiantService;
import com.formation.demo.services.JWTUtils;
import com.formation.demo.services.QuizzService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/student")
@RequiredArgsConstructor
public class EtudiantController {

    private final EtudiantService etudiantService;
    private final QuizzService quizzService;

    private final JWTUtils jwtUtils;

    @GetMapping("")
    ResponseEntity<Object> getAllEtudiants() {
        return etudiantService.getAllEtudiants();
    }

    @PutMapping("")
    ResponseEntity<Etudiant> updateEntity(@RequestBody Etudiant e) {
        return etudiantService.updateStudent(e);
    }

    // @GetMapping("/")
    // public ResponseEntity<Object> getUserByEmail(@RequestParam("token") String
    // token) {
    // String email = jwtUtils.extractUsername(token);
    // return ResponseEntity.ok().body(etudiantService.getUserByEmail(email));
    // }

    @GetMapping("/matieres")
    ResponseEntity<List<Matiere>> listeMatièreByStudent(@RequestHeader("Authorization") String token) {
        try {
            String username = jwtUtils.extractUsername(token);
            return etudiantService.getMatiereByStudent(username);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // permet d'ajouter une matière à la liste des matières d'un étudiants
    @PostMapping("/follow/matiere")
    ResponseEntity suivreMatiere(@RequestParam("matiereId") String idMatiere,
            @RequestParam("Authorization") String token) {
        return etudiantService.followedMatiere(idMatiere, token);
    }

    // envoyer un message

    // @PostMapping("/send/message")

    // suscrire un module
    @PostMapping("/follow/module")
    ResponseEntity suivreModule(@RequestParam("moduleId") String idModule,
            @RequestHeader("Authorization") String token) {
        String userToken = token.split(" ")[1];
        String username = jwtUtils.extractUsername(userToken);
        return etudiantService.suscribeModule(idModule, username);
    }

    // s'inscrire à une promotion
    // @PostMapping("/follow/promotion/{userId}/{promotionId}")
    // ResponseEntity followedPromotion(@PathVariable("promotionId") String id,
    // @PathVariable("userId") String userId) {
    // return etudiantService.joinPromotion(id, userId);
    // }

    // terminer une séance
    @PostMapping("/complete/seance")
    ResponseEntity<?> completeSeance(
            @RequestBody CompleteSeanceModel completeSeanceModel) {
        String userId = completeSeanceModel.getUserId();
        String seanceId = completeSeanceModel.getSeanceId();
        if (quizzService.hasUserSubmittedQuizz(seanceId, userId) == false) {
            return ResponseEntity.badRequest().body("Vous devez d'abord soumettre le quizz associé à cette séance.");
        }

        System.out.println("here");
        System.out.println(completeSeanceModel.getModuleId() + " " + completeSeanceModel.getSeanceId() + " "
                + completeSeanceModel.getUserId());
        if (completeSeanceModel.getModuleId() == null || completeSeanceModel.getSeanceId() == null
                || completeSeanceModel.getUserId() == null) {
            return ResponseEntity.badRequest().build();
        }
        try {
            etudiantService.completeSeance(completeSeanceModel.getSeanceId(),
                    completeSeanceModel.getUserId(),
                    completeSeanceModel.getModuleId());
            return ResponseEntity.ok().build();

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    // verifier si une séance est terminée
    // @GetMapping("/isCompleted/seance")
    // ResponseEntity<Boolean> isCompletedSeance(
    // @RequestParam("moduleId") String moduleId,
    // @RequestParam("seanceId") String seanceId,
    // @RequestHeader("Authorization") String token) {
    // String username = jwtUtils.extractUsername(token);
    // try {
    // boolean isCompleted = etudiantService.isCompletedSeance(moduleId, seanceId,
    // username);
    // return ResponseEntity.ok(isCompleted);
    // } catch (Exception e) {
    // return ResponseEntity.badRequest().build();
    // }
    // }

    // liste des séances terminées pour un module par un étudiant
    @GetMapping("/completed/seances")
    ResponseEntity<List<?>> getCompletedSeances(
            @RequestParam("moduleId") String moduleId,
            @RequestParam("username") String username) {

        try {
            List<?> seances = etudiantService.getCompletedSeances(moduleId, username);
            return ResponseEntity.ok(seances);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // liste des séances terminé par un étudiant
    @GetMapping("/all/completed/seances")
    ResponseEntity<List<?>> getAllCompletedSeances(
            @RequestParam("email") String email) {
        // String userToken = token.split(" ")[1];
        // String username = jwtUtils.extractUsername(userToken);
        try {
            List<?> seances = etudiantService.getAllCompletedSeances(email);
            return ResponseEntity.ok(seances);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }

    }

    // folow Promotion
    @PostMapping("/follow/promotion/{userId}/{promotionId}")
    ResponseEntity<?> followPromotion(@PathVariable("promotionId") String promotionId,
            @PathVariable("userId") String userId) {
        System.out.println(promotionId + " " + userId);
        return etudiantService.followPromotion(promotionId, userId);
    }

    // ne plus sivre une promotion
    @PostMapping("/unfollow/promotion/{userId}/{promotionId}")
    ResponseEntity unfollowPromotion(@PathVariable("promotionId") String promotionId,
            @PathVariable("userId") String userId) {

        return etudiantService.leavePromotion(promotionId, userId);
    }

    // mes promotions
    @GetMapping("/promotions/{userId}")
    ResponseEntity<List<UserFollowPromotionItem>> getMyPromotions(@PathVariable("userId") String userId) {
        try {
            System.out.println("userId: " + userId);
            List<UserFollowPromotionItem> promotions = etudiantService.getMyPromotions(userId);
            return ResponseEntity.ok(promotions);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    // modules progresion
    // @GetMapping("/modules/progression/{userId}/{promotionId}")

}
