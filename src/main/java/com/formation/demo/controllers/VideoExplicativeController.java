package com.formation.demo.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.formation.demo.entities.VideoExplicative;
import com.formation.demo.services.VideoExplicativeService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("api/v1/video-explicative")
@RequiredArgsConstructor
public class VideoExplicativeController {

    private final VideoExplicativeService videoExplicativeService;

    @PostMapping("/create")
    public ResponseEntity<VideoExplicative> createVideoExplicative(@RequestBody VideoExplicative videoExplicative) {
        System.out.println("Creating VideoExplicative: " + videoExplicative);
        VideoExplicative createdVideo = videoExplicativeService.createVideoExplicative(videoExplicative);
        return ResponseEntity.ok(createdVideo);
    }

    @GetMapping("/seance")
    public ResponseEntity<VideoExplicative> getVideoExplicativeBySeanceId(
            @RequestParam("seanceId") String seanceId, @RequestParam("promotionId") String promotionId

    ) {
        VideoExplicative video = videoExplicativeService.getVideoExplicativeBySeanceIdAndPromotionId(seanceId,
                promotionId);
        if (video != null) {
            return ResponseEntity.ok(video);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // suppression d'une vidéo explicative par son ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVideoExplicative(@PathVariable String id) {
        videoExplicativeService.deleteVideoExplicativeById(id);
        return ResponseEntity.ok().build();
    }

}
