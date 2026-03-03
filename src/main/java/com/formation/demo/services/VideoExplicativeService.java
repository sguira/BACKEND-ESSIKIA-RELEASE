package com.formation.demo.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.formation.demo.entities.VideoExplicative;
import com.formation.demo.repository.VideoExplicativeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VideoExplicativeService {

    private final VideoExplicativeRepository videoExplicativeRepository;

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
        videoExplicativeRepository.deleteById(id);
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

}
