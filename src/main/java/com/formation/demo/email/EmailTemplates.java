package com.formation.demo.email;

/**
 * Classe centrale de tous les templates d'email HTML de l'application Essikia.
 * Pour modifier un email, modifier uniquement la méthode correspondante ici.
 */
public class EmailTemplates {

    // ─── Layout de base (utilisé par confirmationCompte, bienvenuFormateur,
    // resetMotDePasse) ─────────────────────────────────────────────────────────
    private static String base(String destinataire, String contenu) {
        return "<!DOCTYPE html>" +
                "<html lang='fr'>" +
                "<head>" +
                "<meta charset='UTF-8'>" +
                "<meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                "<title>Essikia</title>" +
                "</head>" +
                "<body style='margin:0;padding:0;background-color:#f4f6f8;font-family:Arial,Helvetica,sans-serif;color:#1f2933;'>" +
                "<div style='max-width:640px;margin:0 auto;padding:24px;'>" +
                "<div style='background:#0f6d6d;color:#ffffff;padding:24px 28px;border-radius:12px 12px 0 0;'>" +
                "<h1 style='margin:0;font-size:22px;font-weight:700;letter-spacing:0.4px;'>Essikia Formation</h1>" +
                "<p style='margin:6px 0 0;font-size:14px;opacity:0.9;'>Centre de formation & plateforme d'apprentissage</p>" +
                "</div>" +
                "<div style='background:#ffffff;padding:28px;border-radius:0 0 12px 12px;box-shadow:0 10px 30px rgba(15,23,42,0.08);'>" +
                "<p style='margin:0 0 18px;font-size:16px;'>Bonjour <strong>" + destinataire + "</strong>,</p>" +
                "<div style='font-size:15px;line-height:1.6;color:#334e68;'>" + contenu + "</div>" +
                "<div style='margin-top:24px;padding-top:18px;border-top:1px solid #e6e9ef;'>" +
                "<p style='margin:0;font-size:13px;color:#7b8794;'>Si vous avez la moindre question, répondez simplement à cet email.</p>" +
                "</div>" +
                "</div>" +
                "<div style='text-align:center;margin-top:18px;color:#9aa5b1;font-size:12px;'>© 2026 Essikia. Tous droits réservés.</div>" +
                "</div>" +
                "</body>" +
                "</html>";
    }

    // ─── 1. Confirmation de compte ─────────────────────────────────────────────
    // Envoyé à : étudiant ou formateur lors de la création de compte
    // Arguments : nom (prénom + nom), code (code de confirmation à 4 chiffres)
    public static String confirmationCompte(String nom, String code) {
        String contenu = "Veuillez confirmer la création de votre compte sur l'application Essikia " +
                "avec le code suivant :<br><br>" +
                codeBlock(code);
        return base("Monsieur/Madame " + nom, contenu);
    }

    // ─── 2. Bienvenu formateur ─────────────────────────────────────────────────
    // Envoyé à : formateur lors de la création de son compte par l'admin
    // Arguments : nom, motDePasse (mot de passe temporaire), code (code de confirmation)
    public static String bienvenuFormateur(String nom, String motDePasse, String code) {
        String contenu = "Votre compte formateur a été créé avec succès sur la plateforme Essikia.<br>" +
                "Vous pouvez désormais vous connecter et commencer à utiliser nos services.<br><br>" +
                "Votre mot de passe par défaut est : " + codeBlock(motDePasse) +
                "<br>Veuillez confirmer votre compte avec le code : " + codeBlock(code);
        return base(nom, contenu);
    }

    // ─── 3. Réinitialisation de mot de passe ──────────────────────────────────
    // Envoyé à : tout utilisateur ayant demandé un reset de mot de passe
    // Arguments : nom, code (code de réinitialisation à 4 chiffres)
    public static String resetMotDePasse(String nom, String code) {
        String contenu = "Vous avez demandé une réinitialisation de mot de passe.<br>" +
                "Utilisez le code suivant :<br><br>" +
                codeBlock(code) +
                "<br><br>Si vous n'avez pas demandé cette réinitialisation, ignorez cet email.";
        return base(nom, contenu);
    }

    // ─── 4. Notification de planification (envoyée à l'admin) ─────────────────
    // Envoyé à : administrateurs lorsqu'un formateur soumet une nouvelle planification
    // Arguments : nomAdmin, message (texte descriptif), nomSeance, nomModule,
    //             nomPromotion, date (format dd/MM/yyyy HH:mm), nomFormateur
    public static String planificationNotification(String nomAdmin, String message,
            String nomSeance, String nomModule, String nomPromotion,
            String date, String nomFormateur) {
        return "<!DOCTYPE html>" +
                "<html lang='fr'><head><meta charset='UTF-8'>" +
                "<meta name='viewport' content='width=device-width,initial-scale=1.0'>" +
                "<title>Essikia - Planification</title></head>" +
                "<body style='margin:0;padding:0;background-color:#f4f7f9;font-family:\"Segoe UI\",Tahoma,Geneva,Verdana,sans-serif;color:#2d3748;'>" +
                "<div style='max-width:600px;margin:30px auto;padding:0 20px;'>" +
                "<div style='background:linear-gradient(135deg,#0f6d6d 0%,#1a4a4a 100%);color:#fff;padding:45px 30px;border-radius:24px 24px 0 0;text-align:center;'>" +
                "<h1 style='margin:0;font-size:32px;font-weight:800;'>Essikia Formation</h1>" +
                "<p style='margin:12px 0 0;font-size:16px;opacity:0.85;'>Portail d'Administration</p>" +
                "</div>" +
                "<div style='background:#fff;padding:45px 40px;border-radius:0 0 24px 24px;box-shadow:0 20px 25px -5px rgba(0,0,0,0.05);'>" +
                "<p style='margin:0 0 25px;font-size:18px;font-weight:700;color:#1a202c;'>Bonjour " + nomAdmin + ",</p>" +
                "<p style='margin:0 0 30px;font-size:16px;line-height:1.7;color:#4a5568;'>" + message + "</p>" +
                "<div style='background-color:#f8fafc;border:1px solid #edf2f7;border-radius:20px;padding:35px;margin:35px 0;'>" +
                "<h3 style='margin:0 0 25px;font-size:15px;text-transform:uppercase;letter-spacing:1.5px;color:#0f6d6d;'>Détails de la planification</h3>" +
                "<table style='width:100%;border-collapse:collapse;'>" +
                ligneDetails("Séance", nomSeance) +
                ligneDetails("Module", nomModule) +
                ligneDetails("Promotion", nomPromotion) +
                ligneDetails("Date", date) +
                ligneDetails("Formateur", nomFormateur) +
                "</table></div>" +
                "<div style='margin-top:50px;padding-top:30px;border-top:1px solid #edf2f7;text-align:center;'>" +
                "<p style='margin:0;font-size:13px;color:#a0aec0;'>Besoin d'aide ? Répondez simplement à cet email.</p>" +
                "</div></div>" +
                "<div style='text-align:center;margin-top:30px;color:#a0aec0;font-size:12px;'>© 2026 Essikia Formation. Tous droits réservés.</div>" +
                "</div></body></html>";
    }

    // ─── 5. Planification validée (envoyée au formateur) ──────────────────────
    // Envoyé à : formateur lorsque l'admin valide sa planification
    // Arguments : nom, nomSeance, date (format dd/MM/yyyy HH:mm)
    public static String planificationValidee(String nom, String nomSeance, String date) {
        return "<!DOCTYPE html>" +
                "<html lang='fr'><head><meta charset='UTF-8'>" +
                "<meta name='viewport' content='width=device-width,initial-scale=1.0'>" +
                "<title>Séance Validée - Essikia</title></head>" +
                "<body style='margin:0;padding:0;background-color:#f4f7f9;font-family:\"Segoe UI\",Tahoma,Geneva,Verdana,sans-serif;color:#2d3748;'>" +
                "<div style='max-width:600px;margin:40px auto;padding:0 20px;'>" +
                "<div style='background:#fff;padding:40px;border-radius:16px;box-shadow:0 4px 12px rgba(0,0,0,0.08);border-top:5px solid #0f6d6d;'>" +
                "<div style='text-align:center;margin-bottom:30px;'>" +
                "<h2 style='color:#0f6d6d;margin:0;font-size:24px;'>Planification Validée</h2></div>" +
                "<p style='margin:0 0 20px;font-size:16px;'>Bonjour <strong>" + nom + "</strong>,</p>" +
                "<p style='margin:0 0 20px;font-size:16px;line-height:1.6;color:#4a5568;'>" +
                "Votre planification pour la séance <strong>" + nomSeance + "</strong> " +
                "prévue le <strong>" + date + "</strong> a été " +
                "<span style='color:#0f6d6d;font-weight:700;'>validée</span> par l'administration.</p>" +
                "<p style='margin:0 0 30px;font-size:16px;color:#4a5568;'>Elle est désormais visible par les étudiants sur la plateforme.</p>" +
                "<div style='text-align:center;'>" +
                "<a href='#' style='background-color:#0f6d6d;color:#fff;padding:14px 28px;border-radius:8px;text-decoration:none;font-weight:700;display:inline-block;'>Voir mon planning</a>" +
                "</div></div>" +
                "<div style='text-align:center;margin-top:20px;color:#a0aec0;font-size:12px;'>© 2026 Essikia Formation</div>" +
                "</div></body></html>";
    }

    // ─── 6. Blocage ou déblocage de compte ────────────────────────────────────
    // Envoyé à : formateur lors d'un blocage ou déblocage par l'admin
    // Arguments : sujet (titre de l'email), message (explication détaillée)
    public static String gestionCompte(String sujet, String message) {
        return "<html><body style='font-family:Arial,sans-serif;padding:20px;'>" +
                "<h1 style='color:#0f6d6d;'>" + sujet + "</h1>" +
                "<p style='font-size:15px;line-height:1.6;'>" + message + "</p>" +
                "<p style='color:#9aa5b1;font-size:12px;'>© 2026 Essikia Formation</p>" +
                "</body></html>";
    }

    // ─── 7. Suppression de vidéo explicative (envoyée aux admins) ─────────────
    // Envoyé à : administrateurs lorsqu'une vidéo explicative est supprimée
    // Arguments : videoId (identifiant de la vidéo supprimée)
    public static String suppressionVideo(String videoId) {
        return "<html><body style='font-family:Arial,sans-serif;padding:20px;'>" +
                "<h1 style='color:#0f6d6d;'>Suppression de vidéo explicative</h1>" +
                "<p style='font-size:15px;'>La vidéo explicative avec l'ID <strong>" + videoId + "</strong> a été supprimée.</p>" +
                "<p style='color:#9aa5b1;font-size:12px;'>© 2026 Essikia Formation</p>" +
                "</body></html>";
    }

    // ─── 8. Nouvelle planification soumise (notification simple) ──────────────
    // Envoyé à : administrateurs (message court dans le champ bodyEmail.message)
    // Arguments : aucun
    public static String nouvellePlanification() {
        return "<html><body style='font-family:Arial,sans-serif;padding:20px;'>" +
                "<h1 style='color:#0f6d6d;'>Nouvelle planification de séance</h1>" +
                "<p style='font-size:15px;'>Une nouvelle séance a été planifiée. Veuillez consulter votre espace pour plus de détails.</p>" +
                "<p style='color:#9aa5b1;font-size:12px;'>© 2026 Essikia Formation</p>" +
                "</body></html>";
    }

    // ─── Helpers privés ───────────────────────────────────────────────────────

    private static String codeBlock(String valeur) {
        return "<u style='color:red;font-size:26px;'>" + valeur + "</u>";
    }

    private static String ligneDetails(String label, String valeur) {
        return "<tr>" +
                "<td style='padding:12px 0;color:#718096;font-size:14px;width:110px;font-weight:600;'>" + label + "</td>" +
                "<td style='padding:12px 0;color:#1a202c;font-size:16px;font-weight:700;'>" + valeur + "</td>" +
                "</tr>";
    }
}
