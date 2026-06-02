package com.formation.demo.email;

/**
 * Classe centrale de tous les templates d'email HTML de l'application Essikia.
 * Pour modifier un email, modifier uniquement la méthode correspondante ici.
 */
public class EmailTemplates {

        // ─── Layout de base ───────────────────────────────────────────────────────
        private static String base(String destinataire, String contenu) {
                return "<!DOCTYPE html>" +
                                "<html lang='fr'>" +
                                "<head>" +
                                "<meta charset='UTF-8'>" +
                                "<meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                                "<title>ESSIKIA</title>" +
                                "</head>" +
                                "<body style='margin:0;padding:0;background-color:#f4f6f8;font-family:Inter,system-ui,-apple-system,BlinkMacSystemFont,\"Segoe UI\",sans-serif;color:#102a43;'>"
                                +
                                "<div style='max-width:680px;margin:0 auto;padding:24px;'>" +
                                "<div style='background:#0f6d6d;color:#ffffff;padding:28px 32px;border-radius:20px 20px 0 0;'>"
                                +
                                "<h1 style='margin:0;font-size:28px;font-weight:800;letter-spacing:0.6px;'>ESSIKIA</h1>"
                                +
                                "<p style='margin:8px 0 0;font-size:15px;opacity:0.92;'>Programme pédagogique pour maîtriser son argent et son avenir.</p>"
                                +
                                "</div>" +
                                "<div style='background:#ffffff;padding:32px;border-radius:0 0 20px 20px;box-shadow:0 20px 40px rgba(15,23,42,0.08);'>"
                                +
                                "<p style='margin:0 0 20px;font-size:16px;line-height:1.8;color:#102a43;'>Bonjour <strong>"
                                + destinataire + "</strong>,</p>" +
                                "<div style='font-size:15px;line-height:1.8;color:#334e68;'>" + contenu + "</div>" +
                                "</div>" +
                                "<div style='text-align:center;margin-top:22px;color:#8493a6;font-size:12px;'>© 2026 ESSIKIA — Tous droits réservés.</div>"
                                +
                                "</div>" +
                                "<hr style='border:none;border-top:1px solid #e6e9ef;margin:40px 0;'>" +
                                "<div style='max-width:680px;margin:0 auto;padding:0 24px 24px;'>" +
                                "<p style='margin:0;font-size:13px;color:#627d98;'>Cet email a été envoyé automatiquement. Merci de ne pas y répondre directement.<br>Contact : contact@essikia.fr</p>"
                                +
                                "<p style='margin:12px 0 0;font-size:12px;color:#8493a6;'>ESSIKIA est un programme strictement éducatif. Il ne constitue pas un conseil financier, un conseil en gestion de patrimoine ni un conseil en investissement.<br>Conformément au RGPD, vous disposez d'un droit d'accès, de rectification et de suppression de vos données. Pour exercer ces droits : contact@essikia.fr<br>© ESSIKIA — Tous droits réservés.</p>"
                                +
                                "</div>" +
                                "</body>" +
                                "</html>";
        }

        // ─── Email 1 — Code de confirmation d'email ───────────────────────────────
        // Envoyé à : étudiant à l'inscription (compte inactif jusqu'à confirmation)
        // Déclencheur : AuthService.register
        public static String confirmationCompte(String nom, String code) {
                String contenu = "Tu viens de créer ton compte ESSIKIA. Il ne reste plus qu'une étape pour l'activer.<br><br>"
                                +
                                "Voici ton code de confirmation :<br>" +
                                codeBlock(code) +
                                "<br><br>Entre ce code dans l'application pour confirmer ton adresse email et activer ton compte.<br><br>"
                                +
                                "<strong>Ce code est valable 20 minutes.</strong> Passé ce délai, tu devras en demander un nouveau depuis l'application.<br><br>"
                                +
                                "Tu n'as pas créé de compte ESSIKIA ? Ignore simplement cet email.<br><br>" +
                                "L'équipe ESSIKIA.";
                return base(nom, contenu);
        }

        // ─── Email 2 — Bienvenue étudiant ─────────────────────────────────────────
        // Envoyé à : étudiant une fois le compte confirmé et activé
        // Déclencheur : AuthService.confirmeCompte (après validation du code)
        public static String bienvenueEtudiant(String nom, String urlEspace) {
                String contenu = "Ton compte ESSIKIA est activé. Tu rejoins une communauté de jeunes adultes qui ont décidé de reprendre le contrôle de leur argent.<br><br>"
                                +
                                "Voici ce qui t'attend :<br>" +
                                "<ul style='padding-left:18px;color:#334e68;line-height:1.7;'>" +
                                "<li>4 modules progressifs — du budget jusqu'à l'investissement</li>" +
                                "<li>Des cours, des quiz et des fiches mémo pour chaque séance</li>" +
                                "<li>Des outils pratiques directement applicables à ta situation</li>" +
                                "<li>Des intervenants externes et des sessions de coaching privé disponibles</li>" +
                                "<li>Une communauté d'apprentissage pour avancer ensemble</li>" +
                                "</ul><br>" +
                                actionButton("Accéder à mon espace ESSIKIA", urlEspace) +
                                "<br><br>Notre conseil pour bien démarrer : commence par le Module 1, Séance 1. Il dure moins de 30 minutes.<br><br>"
                                +
                                "Pour toute question : contact@essikia.fr<br><br>" +
                                "L'équipe ESSIKIA.";
                return base(nom, contenu);
        }

        // ─── Email 3 — Séance planifiée (étudiant) ────────────────────────────────
        // Envoyé à : étudiant lorsqu'un formateur planifie une séance le concernant
        // Déclencheur : AdminService.validationPlanification (après acceptation admin)
        public static String seancePlanifieeEtudiant(String nom, String formateur, String module, String seance,
                        String date, String heureDebut, String heureFin, String format, String lien) {
                String contenu = "Une nouvelle séance vient d'être planifiée pour toi sur ESSIKIA.<br><br>" +
                                "Détails de la séance :<br>" +
                                "<ul style='padding-left:18px;color:#334e68;line-height:1.7;'>" +
                                "<li><strong>Formateur :</strong> " + formateur + "</li>" +
                                "<li><strong>Module :</strong> " + module + "</li>" +
                                "<li><strong>Séance :</strong> " + seance + "</li>" +
                                "<li><strong>Date :</strong> " + date + "</li>" +
                                "<li><strong>Heure :</strong> " + heureDebut + " – " + heureFin + "</li>" +
                                "<li><strong>Format :</strong> " + format + "</li>" +
                                (lien != null && !lien.isEmpty()
                                                ? "<li><strong>Lien d'accès :</strong> <a href='" + lien
                                                                + "' style='color:#0f6d6d;'>" + lien
                                                                + "</a></li>"
                                                : "")
                                +
                                "</ul><br>" +
                                actionButton("Voir la séance dans mon espace",
                                                lien != null && !lien.isEmpty() ? lien : "#")
                                +
                                "<br><br>Un rappel te sera envoyé 24 heures avant la séance.<br><br>" +
                                "Pour toute question : contact@essikia.fr<br><br>" +
                                "L'équipe ESSIKIA.";
                return base(nom, contenu);
        }

        // ─── Email 4 — Rappel de séance étudiant (24h avant) ─────────────────────
        // Envoyé à : étudiant automatiquement 24h avant une séance planifiée
        // Déclencheur : FormateurService.envoyerRappel
        public static String rappelSeanceEtudiant(String nom, String module, String seance,
                        String date, String heureDebut, String heureFin, String format, String lien) {
                String contenu = "Nous te rappelons que ta séance ESSIKIA a lieu demain.<br><br>" +
                                "Détails :<br>" +
                                "<ul style='padding-left:18px;color:#334e68;line-height:1.7;'>" +
                                "<li><strong>Module :</strong> " + module + "</li>" +
                                "<li><strong>Séance :</strong> " + seance + "</li>" +
                                "<li><strong>Date :</strong> " + date + "</li>" +
                                "<li><strong>Heure :</strong> " + heureDebut + " – " + heureFin + "</li>" +
                                "<li><strong>Format :</strong> " + format + "</li>" +
                                (lien != null && !lien.isEmpty()
                                                ? "<li><strong>Lien d'accès :</strong> <a href='" + lien
                                                                + "' style='color:#0f6d6d;'>" + lien
                                                                + "</a></li>"
                                                : "")
                                +
                                "</ul><br>" +
                                actionButton("Accéder à ma séance", lien != null && !lien.isEmpty() ? lien : "#") +
                                "<br><br>À demain !<br><br>" +
                                "L'équipe ESSIKIA.";
                return base(nom, contenu);
        }

        // ─── Email 5 — Relance inactivité étudiant ────────────────────────────────
        // Envoyé à : étudiant après X jours sans connexion (scheduler externe)
        public static String relanceInactiviteEtudiant(String nom, int joursSansConnexion, String module,
                        String seanceEnCours, int progression, String lien) {
                String contenu = "Cela fait " + joursSansConnexion
                                + " jours que tu n'as pas ouvert ESSIKIA. Pas de jugement — la vie est chargée, on le sait.<br><br>"
                                +
                                "Mais chaque jour passé sans avancer sur tes finances personnelles, c'est du temps que tu ne rattraperas pas. Même 20 minutes peuvent faire la différence.<br><br>"
                                +
                                "Tu en étais là :<br>" +
                                "<ul style='padding-left:18px;color:#334e68;line-height:1.7;'>" +
                                "<li><strong>Module :</strong> " + module + "</li>" +
                                "<li><strong>Séance :</strong> " + seanceEnCours + "</li>" +
                                "<li><strong>Progression :</strong> " + progression + "% du module complété</li>" +
                                "</ul><br>" +
                                actionButton("Reprendre mon parcours", lien) +
                                "<br><br>On t'attend.<br><br>" +
                                "L'équipe ESSIKIA.";
                return base(nom, contenu);
        }

        // ─── Email 6 — Modification de mot de passe ───────────────────────────────
        // Envoyé à : tout utilisateur ayant demandé un reset de mot de passe
        // Déclencheur : AuthService.sendResetCode / resendResetCode
        public static String resetMotDePasse(String nom, String code) {
                String contenu = "Nous avons reçu une demande de modification de mot de passe pour ton compte ESSIKIA.<br><br>"
                                +
                                "Voici ton code de réinitialisation :<br>" +
                                codeBlock(code) +
                                "<br><br><strong>Ce code est valable 24 heures.</strong> Passé ce délai, tu devras faire une nouvelle demande depuis l'application.<br><br>"
                                +
                                "Tu n'es pas à l'origine de cette demande ? Ignore simplement cet email. Ton mot de passe actuel reste inchangé.<br><br>"
                                +
                                "Si tu penses que quelqu'un tente d'accéder à ton compte, contacte-nous immédiatement à contact@essikia.fr.<br><br>"
                                +
                                "L'équipe ESSIKIA.";
                return base(nom, contenu);
        }

        // ─── Email 7 — Séance confirmée par l'admin (formateur) ──────────────────
        // Envoyé à : formateur lorsque l'administrateur confirme sa séance planifiée
        // Déclencheur : AdminService.validationPlanification
        public static String planificationValidee(String nom, String module, String nomSeance, String date,
                        String heureDebut, String heureFin, String format, int apprenants, String lienEspaceFormateur) {
                String contenu = "Bonne nouvelle : votre séance a été confirmée par l'administrateur. Elle est maintenant visible et accessible aux apprenants.<br><br>"
                                +
                                "Détails de la séance confirmée :<br>" +
                                "<ul style='padding-left:18px;line-height:1.7;color:#334e68;'>" +
                                "<li><strong>Module :</strong> " + module + "</li>" +
                                "<li><strong>Séance :</strong> " + nomSeance + "</li>" +
                                "<li><strong>Date :</strong> " + date + "</li>" +
                                "<li><strong>Heure :</strong> " + heureDebut + " – " + heureFin + "</li>" +
                                "<li><strong>Format :</strong> " + format + "</li>" +
                                "<li><strong>Nombre d'apprenants inscrits :</strong> " + apprenants + "</li>" +
                                "</ul><br>" +
                                "Un rappel vous sera envoyé 24 heures avant la séance.<br><br>" +
                                actionButton("Voir ma séance dans mon espace formateur", lienEspaceFormateur) +
                                "<br><br>L'équipe ESSIKIA.";
                return base(nom, contenu);
        }

        // ─── Email 8 — Séance refusée par l'admin (formateur) ────────────────────
        // Envoyé à : formateur lorsque l'administrateur refuse sa séance planifiée
        // Déclencheur : AdminService.anuulerPlanification
        public static String planificationRefuseeFormateur(String nom, String module, String nomSeance, String date,
                        String motif, String lienProposer) {
                String contenu = "Nous vous informons que votre séance planifiée n'a pas pu être confirmée par l'administrateur.<br><br>"
                                +
                                "Séance concernée :<br>" +
                                "<ul style='padding-left:18px;line-height:1.7;color:#334e68;'>" +
                                "<li><strong>Module :</strong> " + module + "</li>" +
                                "<li><strong>Séance :</strong> " + nomSeance + "</li>" +
                                "<li><strong>Date proposée :</strong> " + date + "</li>" +
                                "<li><strong>Motif du refus :</strong> " + motif + "</li>" +
                                "</ul><br>" +
                                actionButton("Proposer une nouvelle date", lienProposer) +
                                "<br><br>Pour toute question : contact@essikia.fr<br><br>" +
                                "L'équipe ESSIKIA.";
                return base(nom, contenu);
        }

        // ─── Email 9 — Rappel de séance formateur (24h avant) ────────────────────
        // Envoyé à : formateur 24h avant la séance qu'il doit animer
        // Déclencheur : FormateurService.envoyerRappel
        public static String rappelSeanceFormateur(String nom, String module, String seance,
                        String date, String heureDebut, String heureFin, String format, int apprenants,
                        String lienEspaceFormateur) {
                String contenu = "Nous vous rappelons que vous animez une séance ESSIKIA demain.<br><br>" +
                                "Détails :<br>" +
                                "<ul style='padding-left:18px;line-height:1.7;color:#334e68;'>" +
                                "<li><strong>Module :</strong> " + module + "</li>" +
                                "<li><strong>Séance :</strong> " + seance + "</li>" +
                                "<li><strong>Date :</strong> " + date + "</li>" +
                                "<li><strong>Heure :</strong> " + heureDebut + " – " + heureFin + "</li>" +
                                "<li><strong>Format :</strong> " + format + "</li>" +
                                "<li><strong>Nombre d'apprenants attendus :</strong> " + apprenants + "</li>" +
                                "</ul><br>" +
                                actionButton("Accéder à mon espace formateur", lienEspaceFormateur) +
                                "<br><br>Pour toute question ou modification de dernière minute : contact@essikia.fr<br><br>"
                                +
                                "L'équipe ESSIKIA.";
                return base(nom, contenu);
        }

        // ─── Email 10 — Séance planifiée par un formateur (admin) ────────────────
        // Envoyé à : administrateur lorsqu'un formateur planifie une séance
        // Déclencheur : PlanificationService.planificationSeance
        public static String planificationNotification(String nomAdmin, String nomSeance, String nomModule,
                        String nomPromotion, String date, String nomFormateur,
                        String heureDebut, String heureFin, String format, int apprenants,
                        String lienConfirmer, String lienRefuser) {
                String contenu = "Un formateur vient de planifier une nouvelle séance. Votre confirmation est requise pour qu'elle soit visible par les apprenants.<br><br>"
                                +
                                "<div style='background:#f3fafb;border:1px solid #cfece6;border-radius:18px;padding:24px;'>"
                                +
                                "<p style='margin:0 0 14px;font-size:15px;font-weight:700;color:#0f6d6d;'>Détails de la séance</p>"
                                +
                                "<ul style='margin:0;padding-left:18px;color:#334e68;line-height:1.7;'>" +
                                "<li><strong>Formateur :</strong> " + nomFormateur + "</li>" +
                                "<li><strong>Module :</strong> " + nomModule + "</li>" +
                                "<li><strong>Séance :</strong> " + nomSeance + "</li>" +
                                "<li><strong>Date :</strong> " + date + "</li>" +
                                "<li><strong>Heure :</strong> " + heureDebut + " – " + heureFin + "</li>" +
                                "<li><strong>Format :</strong> " + format + "</li>" +
                                "<li><strong>Apprenants concernés :</strong> " + apprenants + "</li>" +
                                "<li><strong>Promotion :</strong> " + nomPromotion + "</li>" +
                                "</ul></div><br>" +
                                actionButton("Confirmer la séance", lienConfirmer) + "&nbsp;&nbsp;" +
                                "<a href='" + lienRefuser
                                + "' style='display:inline-block;background:#c53030;color:#ffffff;padding:14px 24px;border-radius:12px;text-decoration:none;font-weight:700;margin:18px 0;'>Refuser la séance</a>"
                                +
                                "<br><br>En cas de refus, le formateur en sera automatiquement notifié.<br><br>" +
                                "L'équipe ESSIKIA.";
                return base(nomAdmin, contenu);
        }

        // ─── Email 11 — Signalement remonté par un utilisateur (admin) ───────────
        // Envoyé à : administrateur lorsqu'un utilisateur signale un problème
        public static String signalementUtilisateur(String prenomNom, String profil, String email,
                        String dateHeure, String type, String description, String adminUrl) {
                String contenu = "Un utilisateur vient de soumettre un signalement depuis l'application ESSIKIA.<br><br>"
                                +
                                "<div style='background:#f3fafb;border:1px solid #cfece6;border-radius:18px;padding:24px;'>"
                                +
                                "<p style='margin:0 0 14px;font-size:15px;font-weight:700;color:#0f6d6d;'>Détails du signalement</p>"
                                +
                                "<ul style='margin:0;padding-left:18px;color:#334e68;line-height:1.7;'>" +
                                "<li><strong>Utilisateur :</strong> " + prenomNom + " — " + profil + "</li>" +
                                "<li><strong>Email :</strong> " + email + "</li>" +
                                "<li><strong>Date :</strong> " + dateHeure + "</li>" +
                                "<li><strong>Type de signalement :</strong> " + type + "</li>" +
                                "</ul></div><br>" +
                                "<p style='color:#334e68;line-height:1.7;'><strong>Description :</strong><br>"
                                + description
                                + "</p><br>" +
                                actionButton("Accéder au détail dans l'espace admin", adminUrl) +
                                "<br><br>Merci de traiter ce signalement dans les meilleurs délais.<br><br>" +
                                "L'équipe ESSIKIA.";
                return base("Administrateur ESSIKIA", contenu);
        }

        // ─── Bienvenue formateur (compte créé par l'admin) ────────────────────────
        // Envoyé à : formateur lors de la création de son compte par l'admin
        // Déclencheur : AuthService.createFormateur
        public static String bienvenuFormateur(String nom, String motDePasse, String code) {
                String contenu = "Votre compte formateur a été créé avec succès sur la plateforme ESSIKIA.<br>" +
                                "Vous pouvez désormais vous connecter et commencer à gérer vos séances.<br><br>" +
                                "Votre mot de passe temporaire est : " + codeBlock(motDePasse) +
                                "<br>Pour activer votre compte, utilisez le code suivant : " + codeBlock(code) +
                                "<br><br><strong>Ce code est valable 20 minutes.</strong> Passé ce délai, demandez un nouveau code depuis l'application.<br><br>"
                                +
                                "Si vous n'avez pas demandé cet accès, ignorez simplement cet email.<br><br>" +
                                "L'équipe ESSIKIA.";
                return base(nom, contenu);
        }

        // ─── Gestion de compte (blocage / déblocage) ──────────────────────────────
        // Envoyé à : formateur lors d'un blocage ou déblocage par l'admin
        public static String gestionCompte(String sujet, String message) {
                String contenu = "<h1 style='margin-top:0;color:#0f6d6d;'>" + sujet + "</h1>" +
                                "<p style='font-size:15px;line-height:1.8;color:#334e68;'>" + message + "</p>";
                return base("Utilisateur Essikia", contenu);
        }

        // ─── Suppression de vidéo explicative ────────────────────────────────────
        // Envoyé à : administrateurs lorsqu'une vidéo explicative est supprimée
        public static String suppressionVideo(String videoId) {
                String contenu = "<h1 style='margin-top:0;color:#0f6d6d;'>Suppression de vidéo explicative</h1>" +
                                "<p style='font-size:15px;line-height:1.8;color:#334e68;'>La vidéo explicative avec l'ID <strong>"
                                + videoId
                                + "</strong> a été supprimée.</p>";
                return base("Administrateur", contenu);
        }

        // email seance annulée : aux étudiants et au formateur, avec les détails de la
        // séance et un message d'excuse
        public static String seanceAnnuleeEtudiant(String module, String seance, String date) {
                String contenu = "Nous sommes désolés de vous informer que la séance suivante a été annulée :<br><br>" +
                                "<ul style='padding-left:18px;line-height:1.7;color:#334e68;'>" +
                                "<li><strong>Module :</strong> " + module + "</li>" +
                                "<li><strong>Séance :</strong> " + seance + "</li>" +
                                "<li><strong>Date :</strong> " + date + "</li>" +
                                // "<li><strong>Heure :</strong> " + heureDebut + "</li>" +
                                "</ul><br>" +
                                "<p style='color:#334e68;line-height:1.8;'><strong>Message de l'administrateur :</strong><br>"

                                + "</p><br>" +
                                "Nous nous excusons pour ce désagrément et vous remercions de votre compréhension.<br><br>"
                                +
                                "L'équipe ESSIKIA.";
                return base("", contenu);
        }

        // ─── Email — Nouvelle promotion disponible (liste d'attente) ─────────────
        // Envoyé à : étudiants en liste d'attente quand une nouvelle promotion s'ouvre
        // Déclencheur : PromotionService.createPromotion
        public static String nouvellePromotionListeAttente(String nom, String nomPromotion,
                        String dateInscription, String dateDebut, String dateFin) {
                String contenu = "Bonne nouvelle ! Une nouvelle promotion ESSIKIA est ouverte aux inscriptions.<br><br>"
                                + "Tu étais sur la liste d'attente et tu es donc prioritaire pour rejoindre cette promotion.<br><br>"
                                + "<div style='background:#f3fafb;border:1px solid #cfece6;border-radius:18px;padding:24px;'>"
                                + "<p style='margin:0 0 14px;font-size:15px;font-weight:700;color:#0f6d6d;'>Détails de la promotion</p>"
                                + "<ul style='margin:0;padding-left:18px;color:#334e68;line-height:1.7;'>"
                                + "<li><strong>Promotion :</strong> " + nomPromotion + "</li>"
                                + "<li><strong>Inscriptions ouvertes jusqu'au :</strong> " + dateInscription + "</li>"
                                + "<li><strong>Début de la formation :</strong> " + dateDebut + "</li>"
                                + "<li><strong>Fin de la formation :</strong> " + dateFin + "</li>"
                                + "</ul></div><br>"
                                + "Connecte-toi à l'application ESSIKIA pour finaliser ton inscription et ton paiement.<br><br>"
                                + "Cette offre est limitée — les places sont réservées en priorité aux étudiants de la liste d'attente.<br><br>"
                                + "L'équipe ESSIKIA.";
                return base(nom, contenu);
        }

        // ─── Email — Démarrage d'une nouvelle promotion ───────────────────────────
        // Envoyé à : étudiants de la liste d'attente quand la promotion démarre
        // Déclencheur : PromotionService (appel manuel ou planifié au démarrage)
        // Params :
        // nom — prénom de l'étudiant
        // nomPromotion — nom de la promotion (ex. "Promo Automne 2026")
        // dateDebut — date de début formatée "dd/MM/yyyy"
        // dateFin — date de fin formatée "dd/MM/yyyy" (peut être "—")
        // modules — liste des noms de modules du programme (peut être vide)
        // formateur — nom du formateur principal (peut être null)
        // urlInscription — lien vers l'application pour s'inscrire
        public static String debutPromotion(String nom, String nomPromotion,
                        String dateDebut, String dateFin,
                        java.util.List<String> modules, String formateur,
                        String urlInscription) {

                // Bloc modules (affiché seulement si la liste n'est pas vide)
                String blocModules = "";
                if (modules != null && !modules.isEmpty()) {
                        StringBuilder li = new StringBuilder();
                        for (String m : modules) {
                                li.append("<li>").append(m).append("</li>");
                        }
                        blocModules = "<p style='margin:12px 0 6px;font-weight:700;color:#0f6d6d;font-size:14px;'>Programme</p>"
                                        + "<ul style='margin:0;padding-left:18px;color:#334e68;line-height:1.8;'>"
                                        + li
                                        + "</ul>";
                }

                // Ligne formateur (optionnelle)
                String ligneFormateur = (formateur != null && !formateur.isBlank())
                                ? "<li><strong>Formateur :</strong> " + formateur + "</li>"
                                : "";

                // Ligne date de fin (optionnelle)
                String ligneDateFin = (dateFin != null && !dateFin.equals("—"))
                                ? "<li><strong>Fin de la formation :</strong> " + dateFin + "</li>"
                                : "";

                String contenu = "C'est le grand départ ! La promotion <strong>" + nomPromotion
                                + "</strong> démarre officiellement et tu fais partie de l'aventure.<br><br>"
                                + "<div style='background:#f3fafb;border:1px solid #cfece6;border-radius:18px;padding:24px;'>"
                                + "<p style='margin:0 0 14px;font-size:15px;font-weight:700;color:#0f6d6d;'>Détails de ta formation</p>"
                                + "<ul style='margin:0;padding-left:18px;color:#334e68;line-height:1.8;'>"
                                + "<li><strong>Promotion :</strong> " + nomPromotion + "</li>"
                                + "<li><strong>Début de la formation :</strong> " + dateDebut + "</li>"
                                + ligneDateFin
                                + ligneFormateur
                                + "</ul>"
                                + blocModules
                                + "</div><br>"
                                + "Connecte-toi à l'application ESSIKIA pour accéder à ton espace, consulter le programme complet et commencer ton parcours.<br><br>"
                                + actionButton("Accéder à mon espace ESSIKIA", urlInscription)
                                + "<br><br>Des questions avant de démarrer ? Écris-nous à <a href='mailto:contact@essikia.fr' style='color:#0f6d6d;'>contact@essikia.fr</a>.<br><br>"
                                + "Bienvenue dans la promotion, et bonne formation !<br><br>"
                                + "L'équipe ESSIKIA.";
                return base(nom, contenu);
        }

        // ─── Email — Module assigné à un formateur dans une promotion ────────────────
        // Envoyé à : formateur quand un module lui est assigné dans une promotion
        // Déclencheur : PromotionService.assignerFormateurAModule
        public static String moduleAssigneFormateur(String nom, String nomModule,
                        String nomPromotion, String dateDebut, String urlEspaceFormateur) {
                String ligneDate = (dateDebut != null && !dateDebut.equals("—"))
                                ? "<li><strong>Début de la formation :</strong> " + dateDebut + "</li>"
                                : "";
                String contenu = "Un module vient de vous être assigné dans le cadre d'une promotion ESSIKIA.<br><br>"
                                + "<div style='background:#f3fafb;border:1px solid #cfece6;border-radius:18px;padding:24px;'>"
                                + "<p style='margin:0 0 14px;font-size:15px;font-weight:700;color:#0f6d6d;'>Détails de l'assignation</p>"
                                + "<ul style='margin:0;padding-left:18px;color:#334e68;line-height:1.8;'>"
                                + "<li><strong>Module :</strong> " + nomModule + "</li>"
                                + "<li><strong>Promotion :</strong> " + nomPromotion + "</li>"
                                + ligneDate
                                + "</ul></div><br>"
                                + "Connectez-vous à votre espace formateur pour consulter le programme, planifier vos séances et prendre contact avec les apprenants.<br><br>"
                                // + actionButton("Accéder à mon espace formateur", urlEspaceFormateur)
                                + "<br><br>Pour toute question : <a href='mailto:contact@essikia.fr' style='color:#0f6d6d;'>contact@essikia.fr</a><br><br>"
                                + "L'équipe ESSIKIA.";
                return base(nom, contenu);

        }

        // ─── Helpers privés ───────────────────────────────────────────────────────

        private static String codeBlock(String valeur) {
                return "<div style='display:inline-block;background:#f8fafc;border:1px solid #d2e8ea;border-radius:12px;padding:14px 20px;margin:16px 0;font-size:24px;font-weight:700;letter-spacing:0.24em;color:#0f6d6d;'>"
                                + valeur + "</div>";
        }

        private static String actionButton(String label, String url) {
                String href = (url == null || url.isEmpty()) ? "#" : url;
                return "<a href='" + href
                                + "' style='display:inline-block;background:#0f6d6d;color:#ffffff;padding:14px 24px;border-radius:12px;text-decoration:none;font-weight:700;margin:18px 0;'>"
                                + label + "</a>";
        }

        // ──────────────────────────────────────────────────────────────────────────────

}
