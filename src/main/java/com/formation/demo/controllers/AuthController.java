package com.formation.demo.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.formation.demo.dto.ConfirmCompteResponse;
import com.formation.demo.dto.ExtraUserData;
import com.formation.demo.dto.RecupP;
import com.formation.demo.dto.RecupPassword;
import com.formation.demo.dto.ResetPasswordDTO;
import com.formation.demo.entities.Fichiers;
import com.formation.demo.entities.Formateur;
import com.formation.demo.entities.Utilisateur;
import com.formation.demo.services.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody Utilisateur u) {
        return authService.login(u);
    }

    @PostMapping("/register/admin")
    public ResponseEntity<Object> registerAdmin(@RequestBody Utilisateur utilisateur) {
        // TODO: process POST request

        try {
            return authService.createAdmin(utilisateur);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody Utilisateur u) {
        System.out.println("mot de passe" + u.getPassword());
        return authService.register(u);
    }

    @PostMapping("/register-formateur")
    public ResponseEntity<Object> registerFormateur(@RequestBody Formateur u) {
        System.out.println("mot de passe" + u.getPassword());
        return authService.createFormateur(u);
    }

    @GetMapping("/confirm-student")
    ResponseEntity<ConfirmCompteResponse> confirmCompte(
            @RequestParam("email") String email,
            @RequestParam("code") String code) {
        System.out.println("Code:" + code);
        return authService.confirmeCompte(email, code);
    }

    @PostMapping("/extraData")
    ResponseEntity<Object> updateExtraData(
            @RequestParam("email") String email,
            @RequestBody ExtraUserData extra) {
        return authService.updateExtraData(email, extra.getAvatar(), extra.getInfo());
    }

    @GetMapping("/verify-token")
    ResponseEntity<Object> verifyToken(@RequestParam("token") String token) {
        return authService.verifyToken(token);
    }

    @PostMapping("/send-reset-code")
    ResponseEntity<Object> sendResetCode(@RequestParam("email") String email) {
        return authService.sendResetCode(email);
    }

    @PostMapping("/resend-reset-code")
    ResponseEntity<Object> resendResetCode(@RequestParam("email") String email) {
        try {
            authService.resendResetCode(email);
            return ResponseEntity.ok().body("Code de réinitialisation renvoyé avec succès");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de l'envoi du code de réinitialisation");
        }
    }

    @PostMapping("/verify-reset-code")
    ResponseEntity<Object> resetPassword(@RequestBody ResetPasswordDTO resetPasswordDTO) {
        return authService.verifyResetCode(resetPasswordDTO.getEmail(), resetPasswordDTO.getCode());
    }

    // reset password
    @PostMapping("/reset-password")
    ResponseEntity<Object> resetPassword(@RequestBody RecupPassword recupP) {
        return authService.changePassword(recupP.getEmail(), recupP.getNewPassword(), recupP.getCode());
    }

}
