package com.formation.demo.services;

// import java.lang.ProcessHandle.Info;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.formation.demo.dto.ConfirmCompteResponse;
import com.formation.demo.dto.Info;
import com.formation.demo.email.BodyEmail;
import com.formation.demo.email.EmailServiceImp;
import com.formation.demo.entities.Etudiant;
import com.formation.demo.entities.Fichiers;
import com.formation.demo.entities.Formateur;
import com.formation.demo.entities.Utilisateur;
import com.formation.demo.repository.EtudiantRepo;
import com.formation.demo.repository.FichierRepo;
import com.formation.demo.repository.FormateurRepo;
import com.formation.demo.repository.UtilisateurRepo;
import lombok.RequiredArgsConstructor;

@Service
@Component
@RequiredArgsConstructor
public class AuthService {

    private final UtilisateurRepo utilisateurRepo;
    private final EtudiantRepo etudiantRepo;
    private final PasswordEncoder passwordEncoder;
    private final FormateurRepo formateurRepo;
    private final JWTUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final EmailServiceImp emailService;
    private final FichierRepo fichierRepo;

    @Transactional
    public ResponseEntity<Object> register(Utilisateur utilisateur) {

        System.out.println("Mot de passe " + utilisateur.getPassword() + "\n\n");

        Utilisateur u = utilisateurRepo.findByEmail(utilisateur.getEmail()).orElse(null);
        // System.out.println(u.getEmail());
        if (u != null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("L'utilisateur existe déja");
        }
        utilisateur.setPassword(passwordEncoder.encode(utilisateur.getPassword()));
        if (utilisateur.getType().equals("etudiant")) {
            utilisateur.setRole("ROLE_ETUDIANT");

            BodyEmail email = new BodyEmail();

            email.setRecipient(utilisateur.getEmail());
            Etudiant e = createEtudiant(utilisateur);
            // utilisateurRepo.save(utilisateur);
            e.setCodeConfirm(generateCode());
            email.setBody("Création de compte");
            String res = emailService.sendHtlmlMail(email, creerHtmlBody(u, "Monsieur/Madame" + utilisateur.getNom() +
                    "Veuillez confirmer la création de votre compte sur L'application Essikia, <br>" +
                    "avec le code suivant <u style='color: red;font-size: 26px;'>" + e.getCodeConfirm() + "</u>"

            ));
            System.out.println("Mon Mail :" + res);
            if (res.equals("Mail Sent Successfully...") || true) {
                utilisateurRepo.save(utilisateur);
                etudiantRepo.save(e);
                return ResponseEntity.status(HttpStatus.CREATED).build();

            }

        }
        if (utilisateur.getType().equals("formateur")) {
            utilisateur.setRole("ROLE_FORMATEUR");
            utilisateurRepo.save(utilisateur);
            formateurRepo.save((Formateur) utilisateur);
        }
        return ResponseEntity.ok("Utilisateur cree avec succes");
    }

    public ResponseEntity<Object> createAdmin(Utilisateur u) throws Exception {
        try {
            Utilisateur user = utilisateurRepo.findByEmail(u.getEmail()).orElse(null);
            if (user != null) {
                throw new Exception("Utilisateur existe deja");
            }
            u.setPassword(passwordEncoder.encode(u.getPassword()));
            u.setRole("ROLE_ADMIN");
            u.setIsConfirmed(true);
            utilisateurRepo.save(u);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    String generateCode() {
        Random rand = new Random();
        String numero = "0123456789";
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < 4; i++) {
            int val = rand.nextInt(numero.length());
            stringBuilder.append(numero.charAt(val));
        }
        return stringBuilder.toString();

    }

    private Etudiant createEtudiant(Utilisateur u) {
        Etudiant e = new Etudiant();
        e.setAdresse(u.getAdresse());
        e.setPassword(u.getPassword());
        e.setEmail(u.getEmail());
        e.setNom(u.getNom());
        e.setPrenom(u.getPrenom());
        e.setRole(u.getRole());
        e.setType(u.getType());
        return e;
    }

    String creerHtmlBody(Utilisateur users, String message) {
        String displayName = "";
        if (users != null) {
            String nom = users.getNom() != null ? users.getNom() : "";
            String prenom = users.getPrenom() != null ? users.getPrenom() : "";
            displayName = (prenom + " " + nom).trim();
        }
        if (displayName.isEmpty()) {
            displayName = "Client";
        }

        String body = "";
        body += "<!DOCTYPE html>" +
                "<html lang='fr'>" +
                "<head>" +
                "<meta charset='UTF-8'>" +
                "<meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                "<title>Essikia</title>" +
                "</head>" +
                "<body style='margin:0;padding:0;background-color:#f4f6f8;font-family:Arial,Helvetica,sans-serif;color:#1f2933;'>"
                +
                "<div style='max-width:640px;margin:0 auto;padding:24px;'>" +
                "<div style='background:#0f6d6d;color:#ffffff;padding:24px 28px;border-radius:12px 12px 0 0;'>" +
                "<h1 style='margin:0;font-size:22px;font-weight:700;letter-spacing:0.4px;'>Essikia Formation</h1>" +
                "<p style='margin:6px 0 0;font-size:14px;opacity:0.9;'>Centre de formation & plateforme d'apprentissage</p>"
                +
                "</div>" +
                "<div style='background:#ffffff;padding:28px;border-radius:0 0 12px 12px;box-shadow:0 10px 30px rgba(15,23,42,0.08);'>"
                +
                "<p style='margin:0 0 18px;font-size:16px;'>Bonjour <strong>" + displayName + "</strong>,</p>" +
                "<div style='font-size:15px;line-height:1.6;color:#334e68;'>" +
                message +
                "</div>" +
                "<div style='margin-top:24px;padding-top:18px;border-top:1px solid #e6e9ef;'>" +
                "<p style='margin:0;font-size:13px;color:#7b8794;'>Si vous avez la moindre question, répondez simplement à cet email.</p>"
                +
                "</div>" +
                "</div>" +
                "<div style='text-align:center;margin-top:18px;color:#9aa5b1;font-size:12px;'>" +
                "© 2026 Essikia. Tous droits réservés." +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";

        return body;
    }

    public ResponseEntity<Object> createFormateur(Formateur formateur) {
        System.out.println("Creation formateur ..." + formateur.getEmail());
        Utilisateur utilisateur = utilisateurRepo.findByEmail(formateur.getEmail()).orElse(null);
        if (utilisateur != null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("L'utilisateur existe déja");
        }

        try {
            formateur.setPassword(passwordEncoder.encode(formateur.getPassword()));
            formateur.setIsConfirmed(false);
            formateur.setType("formateur");
            formateur.setRole("ROLE_ENSEIGNANT");
            formateur.setCodeConfirm(generateCode());
            utilisateurRepo.save(formateur);
            BodyEmail email = new BodyEmail();
            email.setRecipient(formateur.getEmail());
            email.setBody("Création de compte");

            String res = emailService.sendHtlmlMail(email,
                    creerHtmlBody(formateur, "Bienvenue Monsieur/Madame " + formateur.getNom() +
                            "<br> Votre compte formateur a été créé avec succès sur la plateforme Essikia.<br>" +
                            "Vous pouvez désormais vous connecter et commencer à utiliser nos services.<br>"
                            + "Veuillez confirmer votre compte en utilisant le code suivant : <u style='color: red;font-size: 26px;'>"
                            + formateur.getCodeConfirm() + "</u><br>"));

            if (!res.equals("Mail Sent Successfully...")) {
                throw new Exception("Erreur lors de l'envoi de l'email");
            }

            return ResponseEntity.ok().body(formateurRepo.save(formateur));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Une erreur est survenue");
        }
    }

    public ResponseEntity<Object> login(Utilisateur u) {
        Map<String, Object> result = new HashMap<>();
        try {

            Utilisateur ut = utilisateurRepo.findByEmail(u.getEmail()).orElse(null);
            if (ut == null) {
                // result.put("code", -1);
                // result.put("message", "Aucun compte trouvé");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Aucun compte trouvé");
            }

            if (ut.isBlocked()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Votre compte est bloqué, veuillez contacter l'administrateur");
            }

            if (ut.getRecupPassword() != null && passwordEncoder.matches(u.getPassword(), ut.getRecupPassword())) {
                Map<String, Object> response = new HashMap<>();
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }
            if (ut.getRole().equals("ROLE_ADMIN")) {
                if (passwordEncoder.matches(u.getPassword(), ut.getPassword())) {
                    Authentication authentication = authenticationManager
                            .authenticate(
                                    new UsernamePasswordAuthenticationToken(u.getEmail(), u.getPassword()));
                    if (authentication.isAuthenticated()) {

                        String token = jwtUtils.generateToken(u.getEmail());
                        result.put("token", token);
                        result.put("user", ut);
                        result.put("code", 1);
                        result.put("ROLE", "ADMIN");
                        return ResponseEntity.ok(result);
                    }
                } else {
                    return ResponseEntity.status(400).body("Problème de connexion");
                }
            }
            System.out.println("Utilisateur trouvé :" + ut.getEmail());
            System.out.println("Mot de passe fourni :" + u.getPassword());
            System.out.println("Type:" + ut.getType());

            String type = ut.getType();
            if (type.equals("etudiant")) {
                Etudiant etudiant = etudiantRepo.findByEmail(u.getEmail());
                System.out.println("Etudiant trouvé :" + etudiant.getEmail());
                if (etudiant != null && passwordEncoder.matches(u.getPassword(), etudiant.getPassword())) {
                    // String token = jwtUtils.generateToken(etudiant.getId());
                    System.out.println("Authentifié" + etudiant.getEmail());
                    if (ut.getIsConfirmed() == true) {
                        Authentication authentication = authenticationManager
                                .authenticate(
                                        new UsernamePasswordAuthenticationToken(u.getEmail(), u.getPassword()));
                        if (authentication.isAuthenticated()) {
                            String token = jwtUtils.generateToken(etudiant.getEmail());
                            Utilisateur utilisateur = utilisateurRepo.findByEmail(etudiant.getEmail()).orElse(null);
                            result.put("token", token);
                            result.put("user", utilisateur);
                            result.put("code", 1);
                            result.put("ROLE", "ETUDIANT");
                            return ResponseEntity.ok(result);
                        }
                    } else {
                        Utilisateur utilisateur = utilisateurRepo.findByEmail(etudiant.getEmail()).orElse(null);
                        result.put("message", "Compte non confirmé");
                        result.put("user", utilisateur);
                        result.put("code", -1);
                        return ResponseEntity.ok(result);
                    }
                }
                result.put("code", -1);
                result.put("message", "Aucun compte trouvé");
                return ResponseEntity.status(HttpStatus.OK).body(result);
            } else {
                if (type.equals("formateur")) {
                    Formateur formateur = formateurRepo.findByEmail(u.getEmail());
                    System.out.println("Formateur trouvé :" + formateur.getEmail());
                    if (formateur != null && passwordEncoder.matches(u.getPassword(), ut.getPassword())) {
                        // String token = jwtUtils.generateToken(etudiant.getId());

                        if (ut.getIsConfirmed()) {
                            Authentication authentication = authenticationManager
                                    .authenticate(
                                            new UsernamePasswordAuthenticationToken(u.getEmail(), u.getPassword()));
                            if (authentication.isAuthenticated()) {
                                String token = jwtUtils.generateToken(formateur.getEmail());
                                result.put("token", token);
                                result.put("user", formateur);
                                result.put("code", 1);
                                result.put("ROLE", "FORMATEUR");
                                return ResponseEntity.ok(result);
                            }
                        } else {
                            result.put("message", "Compte non confirmé");
                            result.put("code", -1);
                            result.put("user", formateur);
                            return ResponseEntity.status(HttpStatus.OK).body(result);
                        }
                    } else {
                        result.put("message", "Compte non trouvé");
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Aucun compte trouvé");
                    }
                }
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            result.put("message", "Erreur interne");
            result.put("code", -1);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Une erreur est survenue.");
        }

    }

    public ResponseEntity<ConfirmCompteResponse> confirmeCompte(String email, String code) {
        try {
            Utilisateur utilisateur = utilisateurRepo.findByEmail(email).orElse(null);
            if (utilisateur == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            if (utilisateur != null) {
                String type = utilisateur.getType();
                if (type.equals("etudiant")) {
                    Etudiant etudiant = etudiantRepo.findByEmail(email);
                    utilisateur.setConfirmed(true);
                    utilisateur.setIsConfirmed(true);

                    if (utilisateur.getCodeConfirm().equals(code)) {
                        etudiant.setConfirmed(true);
                        etudiantRepo.save(etudiant);
                        utilisateurRepo.save(utilisateur);
                        etudiantRepo.save(etudiant);
                        ConfirmCompteResponse response = new ConfirmCompteResponse();
                        response.setRole(utilisateur.getRole());
                        response.setEmail(utilisateur.getEmail());
                        System.out.println("Compte confirmé pour l'étudiant avec email: " + email);
                        return ResponseEntity.ok(response);
                    } else {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
                    }
                }
                if (type.equals("formateur")) {
                    Formateur formateur = formateurRepo.findByEmail(email);
                    if (formateur == null) {
                        System.out.println("Formateur non trouvé avec email: " + email);
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
                    }
                    if (utilisateur.getCodeConfirm().equals(code)) {
                        utilisateur.setConfirmed(true);
                        formateur.setConfirmed(true);

                        formateur.setCodeConfirm("");
                        formateurRepo.save(formateur);
                        utilisateurRepo.save(utilisateur);
                        ConfirmCompteResponse response = new ConfirmCompteResponse();
                        response.setRole(utilisateur.getRole());
                        response.setEmail(utilisateur.getEmail());
                        System.out.println("Compte confirmé pour formateur avec email: " + email + "role: "
                                + utilisateur.getRole());
                        return ResponseEntity.ok(response);
                    } else {
                        System.out.println("Code de confirmation incorrect pour formateur avec email: " + email);
                        return ResponseEntity.badRequest().body(null);
                    }
                }
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

    }

    public ResponseEntity<Object> updateExtraData(String email, Fichiers fichier, Info extra) {

        try {
            Utilisateur utilisateur = utilisateurRepo.findByEmail(email).orElse(null);
            if (utilisateur == null) {
                throw new Exception("L'utilisateur n'existe pas");
            }

            if (utilisateur.getRole().equals("ROLE_ETUDIANT")) {
                Etudiant etudiant = etudiantRepo.findByEmail(email);
                if (etudiant == null) {
                    throw new Exception("L'utilisateur n'existe pas");
                }
                if (!utilisateur.getIsConfirmed()) {
                    return ResponseEntity.status(404).body("Utilisateur non confirmé");
                }
                if (fichier != null) {
                    Fichiers fichiers = fichierRepo.save(fichier);
                    utilisateur.ajouterAvatar(fichiers);
                    etudiant.ajouterAvatar(fichiers);
                }
                etudiant.setDefaults(extra.getDefauts());
                etudiant.setQualites(extra.getQualite());
                etudiant.setProfession(extra.getProfession());
                etudiant.setObjectif(extra.getObjectif());

                return ResponseEntity.ok().body(etudiantRepo.save(etudiant));
            }

            if (utilisateur.getRole().equals("ROLE_ENSEIGNANT")) {
                Formateur formateur = formateurRepo.findByEmail(email);
                if (formateur == null) {
                    throw new Exception("L'utilisateur n'existe pas");
                }
                if (!utilisateur.getIsConfirmed()) {
                    return ResponseEntity.status(404).body("Utilisateur non confirmé");
                }
                if (fichier != null) {
                    Fichiers fichiers = fichierRepo.save(fichier);
                    utilisateur.ajouterAvatar(fichiers);
                    formateur.ajouterAvatar(fichiers);
                }
                formateur.setProfession(extra.getProfession());
                formateur.setObjectif(extra.getObjectif());

                return ResponseEntity.ok().body(formateurRepo.save(formateur));
            }
            throw new Exception("Role non supporté");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Une erreur est survenue");
        }
    }

    public ResponseEntity<Object> verifyToken(String token) {
        try {
            String email = jwtUtils.extractUsername(token);
            Utilisateur u = utilisateurRepo.findByEmail(email).orElse(null);
            if (u == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilisateur non trouvé");
            }
            Map<String, Object> result = new HashMap<>();
            result.put("user", u);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token invalide");
        }
    }

    // send reset code
    public ResponseEntity<Object> sendResetCode(String email) {
        try {
            Utilisateur u = utilisateurRepo.findByEmail(email).orElse(null);
            if (u == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilisateur non trouvé");
            }
            String code = generateCode();
            u.setResetCode(code);
            utilisateurRepo.save(u);

            BodyEmail emailBody = new BodyEmail();
            emailBody.setRecipient(email);
            emailBody.setBody("Réinitialisation de mot de passe");
            String res = emailService.sendHtlmlMail(emailBody,
                    creerHtmlBody(u, "Bonjour " + u.getNom() +
                            "<br> Vous avez demandé une réinitialisation de mot de passe. <br>" +
                            "Utilisez le code suivant pour réinitialiser votre mot de passe : <u style='color: red;font-size: 26px;'>"
                            + code + "</u><br>" +
                            "Si vous n'avez pas demandé cette réinitialisation, veuillez ignorer cet email.<br>"));

            if (!res.equals("Mail Sent Successfully...")) {
                throw new Exception("Erreur lors de l'envoi de l'email");
            }

            return ResponseEntity.ok().body("Code de réinitialisation envoyé avec succès");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Une erreur est survenue");
        }
    }

    public ResponseEntity<Object> verifyResetCode(String email, String code) {
        try {
            System.out.println("Email: " + email);
            System.out.println("Code: " + code);
            Utilisateur u = utilisateurRepo.findByEmail(email).orElse(null);
            if (u == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilisateur non trouvé");
            }
            if (!u.getResetCode().equals(code)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Code de réinitialisation incorrect");
            }
            // String newPassword = generateRandomPassword();

            // BodyEmail emailBody = new BodyEmail();
            // emailBody.setRecipient(email);
            // emailBody.setBody("Réinitialisation de mot de passe");
            // String res = emailService.sendHtlmlMail(emailBody,
            // creerHtmlBody(u, "Bonjour " + u.getNom() +
            // "<br> Votre mot de passe a été réinitialisé avec succès. <br>" +
            // "Votre nouveau mot de passe est : <u style='color: red;font-size: 26px;'>" +
            // newPassword
            // + "</u><br>" +
            // "Veuillez vous connecter et changer votre mot de passe dès que
            // possible.<br>"));

            utilisateurRepo.save(u);
            return ResponseEntity.ok().body("Mot de passe réinitialisé avec succès");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Une erreur est survenue");
        }
    }

    String generateRandomPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder password = new StringBuilder();
        Random rnd = new Random();
        for (int i = 0; i < 10; i++) {
            password.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return password.toString();
    }

    // change recup password to null
    public ResponseEntity<Object> clearRecupPassword(String email, String newPassword, String recupPassword) {
        try {
            Utilisateur u = utilisateurRepo.findByEmail(email).orElse(null);
            if (u == null) {
                System.out.println("Utilisateur non trouvé pour l'email : " + email);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilisateur non trouvé");
            }
            if (!passwordEncoder.matches(recupPassword, u.getRecupPassword())) {

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Mot de passe temporaire incorrect");
            }
            u.setRecupPassword(null);
            String encodedPassword = passwordEncoder.encode(newPassword);
            u.setPassword(encodedPassword);
            utilisateurRepo.save(u);

            // Mettre à jour aussi l'entité enfant (Etudiant ou Formateur)
            if (u.getType().equals("etudiant")) {
                Etudiant etudiant = etudiantRepo.findByEmail(email);
                if (etudiant != null) {
                    etudiant.setPassword(encodedPassword);
                    etudiantRepo.save(etudiant);
                }
            } else if (u.getType().equals("formateur")) {
                Formateur formateur = formateurRepo.findByEmail(email);
                if (formateur != null) {
                    formateur.setPassword(encodedPassword);
                    formateurRepo.save(formateur);
                }
            }

            return ResponseEntity.ok().body("Mot de passe temporaire supprimé avec succès");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Une erreur est survenue");
        }
    }

    // new password
    @Transactional
    public ResponseEntity<Object> changePassword(String email, String newPassword, String code) {
        try {
            Utilisateur u = utilisateurRepo.findByEmail(email).orElse(null);
            System.out.println("Email: " + email);
            System.out.println("Code: " + code);

            if (u == null) {
                System.out.println("Utilisateur non trouvé pour l'email : " + email);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilisateur non trouvé");
            }
            System.out.println("Code " + u.getResetCode());
            if (!u.getResetCode().equals(code)) {
                System.out.println("Code de réinitialisation incorrect pour l'email : " + email);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Code de réinitialisation incorrect");
            }
            System.out.println("Nouveau mot de passe : " + newPassword);
            String encodedPassword = passwordEncoder.encode(newPassword);
            System.out.println("Mot de passe encodé : " + encodedPassword);
            u.setPassword(encodedPassword);
            utilisateurRepo.save(u);

            // Mettre à jour aussi l'entité enfant (Etudiant ou Formateur)
            if (u.getType().equals("etudiant")) {
                Etudiant etudiant = etudiantRepo.findByEmail(email);
                if (etudiant != null) {
                    etudiant.setPassword(encodedPassword);
                    etudiantRepo.save(etudiant);
                }
            } else if (u.getType().equals("formateur")) {
                Formateur formateur = formateurRepo.findByEmail(email);
                if (formateur != null) {
                    formateur.setPassword(encodedPassword);
                    formateurRepo.save(formateur);
                }
            }

            return ResponseEntity.ok().body("Mot de passe changé avec succès");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Une erreur est survenue");
        }
    }
}
