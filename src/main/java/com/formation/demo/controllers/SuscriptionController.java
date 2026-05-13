package com.formation.demo.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.formation.demo.dto.PromotionSuscriptionDTO;
import com.formation.demo.dto.SuscriptionDTO;
import com.formation.demo.entities.Suscription;
import com.formation.demo.services.SuscriptionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/suscription")
@RequiredArgsConstructor
public class SuscriptionController {

    private final SuscriptionService suscriptionService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody SuscriptionDTO suscription) throws Exception {
        try {
            System.out.println("Received subscription request email: " + suscription.getEmail() + ", offreId: "
                    + suscription.getOffreId() + ", paiementIntent: " + suscription.getPaiementIntent());
            return ResponseEntity.ok(suscriptionService.suscription(suscription));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Subscription failed");
        }
    }

    @GetMapping("/verify/{userId}/{moduleId}")
    public ResponseEntity<?> verifySubscription(@PathVariable String userId, @PathVariable String moduleId) {
        boolean isSubscribed = suscriptionService.isSubscribed(userId, moduleId);
        if (isSubscribed) {
            return ResponseEntity.ok().body(true);
        } else {
            return ResponseEntity.status(400).body(false);
        }
    }

    @GetMapping("/status/{userId}/{promotionId}")
    public ResponseEntity<?> verifyPromotionSubscription(@PathVariable String userId,
            @PathVariable String promotionId) {
        boolean hasSubscribed = suscriptionService.hasSuscribepromotion(userId, promotionId);
        if (hasSubscribed) {
            return ResponseEntity.ok().body(true);
        } else {
            return ResponseEntity.status(400).body(false);
        }
    }

    @PostMapping("/module-suscription")
    public ResponseEntity<?> createModuleSuscription(@RequestBody PromotionSuscriptionDTO suscription) {
        try {
            suscriptionService.addPromotionSouscription(suscription);
            return ResponseEntity.ok("Souscription validée");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{email}")
    public ResponseEntity<?> getUserSuscriptions(@PathVariable String email) {
        try {
            System.out.println("Fetching subscriptions for user ID: " + email);
            return ResponseEntity.ok(suscriptionService.getUserSuscriptions(email));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/my-offre/{email}")
    public ResponseEntity<?> getUserOffre(@PathVariable String email) {
        try {
            return ResponseEntity.ok(suscriptionService.getUserOffre(email));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

}