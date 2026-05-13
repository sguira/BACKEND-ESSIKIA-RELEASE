package com.formation.demo.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.formation.demo.email.BodyEmail;
import com.formation.demo.email.EmailServiceImp;
import com.formation.demo.entities.Utilisateur;
import com.formation.demo.entities.VideoExplicative;
import com.formation.demo.repository.VideoExplicativeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VideoExplicativeService {

    private final VideoExplicativeRepository videoExplicativeRepository;
    private final EmailServiceImp emailService;
    private final UtilisateurService utilisateurService;

    // ajout de video explicative
    public VideoExplicative createVideoExplicative(VideoExplicative videoExplicative) {
        return videoExplicativeRepository.save(videoExplicative);
    }

    // recuperation de la video explicative par seanceId
    public VideoExplicative getVideoExplicativeBySeanceId(String seanceId) {
        return videoExplicativeRepository.findBySeanceId(seanceId);
    }

    // suppression de la video explicative par id
    public void deleteVideoExplicativeById(String id) {
        try {

            List<Utilisateur> admins = utilisateurService.getAllAdmin("ADMIN");
            String adminEmail = "soloadin@gmail.com"; // Remplacez par l'adresse email de l'administrateur
            String subject = "Suppression de vidéo explicative";
            String message = generateHtmlmessage(id);
            BodyEmail bodyEmail = new BodyEmail();
            bodyEmail.setRecipient(adminEmail);
            bodyEmail.setBody(subject);
            bodyEmail.setMessage(message);
            for (Utilisateur admin : admins) {
                bodyEmail.setRecipient(admin.getEmail());
                emailService.sendHtlmlMail(bodyEmail, message);
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de la suppression de la vidéo explicative: " + e.getMessage());
        } finally {
            System.out.println("Suppression de la vidéo explicative avec l'ID: " + id);
            videoExplicativeRepository.deleteById(id);
        }

    }

    // retrouver une vidéo pour la séance d'une promootion

    public VideoExplicative getVideoExplicativeBySeanceIdAndPromotionId(String seanceId, String promotionId) {
        List<VideoExplicative> optionalVideo = new ArrayList<>();

        for (VideoExplicative video : videoExplicativeRepository.findAll()) {
            if (video.getSeanceId().equals(seanceId) && video.getPromotionId().equals(promotionId)) {
                optionalVideo.add(video);
            }
        }

        if (!optionalVideo.isEmpty()) {
            return optionalVideo.get(optionalVideo.size() - 1); // Retourne la dernière vidéo trouvée
        }
        return null;
    }

    // generation de mail de notification pour la suppression d'une vidéo
    // explicative
    public String generateHtmlmessage(String videoId) {
        return "<html>" +
                "<body>" +
                "<h1>Suppression de vidéo explicative</h1>" +
                "<p>La vidéo explicative avec l'ID " + videoId + " a été supprimée.</p>" +
                "</body>" +
                "</html>";
    }
}
