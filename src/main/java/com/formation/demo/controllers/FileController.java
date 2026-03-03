package com.formation.demo.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.formation.demo.services.FileService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/uploads")
public class FileController {

    private final FileService fileService;

    @PostMapping()
    public ResponseEntity<String> saveFile(@RequestParam("file") MultipartFile file) {
        try {
            return fileService.uploadFile(file);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error");
        }
    }

}
