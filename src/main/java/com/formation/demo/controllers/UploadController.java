package com.formation.demo.controllers;

import java.io.IOException;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.formation.demo.services.SupaBaseService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/upload")
@RequiredArgsConstructor
public class UploadController {

    private final SupaBaseService fileService;

    @PostMapping("")
    public ResponseEntity<Object> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            Object result = fileService.uploadFile(file);
            return ResponseEntity.ok().body(result);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erreur de chargement du fichier" + e.getMessage());
        }
    }

    @GetMapping("/signed-url")
    public ResponseEntity<?> getSignedUrl(@RequestParam String filename) throws IOException {
        System.out.println("Received filename: " + filename);
        String filePath = "uploads/" + System.currentTimeMillis() + "_" + filename;

        String signedUrl = fileService.generateSignedUploadUrl(filePath);
        System.out.println("Generated signed URL: " + signedUrl);
        return ResponseEntity.ok(Map.of(
                "signedUrl", signedUrl,
                "filePath", filePath));
    }

}
