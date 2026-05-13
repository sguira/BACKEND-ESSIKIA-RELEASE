package com.formation.demo.controllers;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.formation.demo.services.R2Service;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/uploads/r2")
@RequiredArgsConstructor
public class R2UploadController {

    private final R2Service r2Service;

    // 🔼 Upload (équivalent Supabase signed upload)
    @GetMapping("/signed-upload")
    public ResponseEntity<?> getSignedUpload(@RequestParam String filename) {
        System.out.println("Received filename: " + filename);
        return ResponseEntity.ok(
                r2Service.generateSignedUploadUrl(filename));
    }

    // 🎥 Lecture vidéo
    @GetMapping("/video-url")
    public ResponseEntity<?> getVideo(@RequestParam String key) {

        String url = r2Service.generateSignedVideoUrl(key);
        System.out.println("Generated video URL: " + url);
        return ResponseEntity.ok(Map.of("url", url));
    }

}
