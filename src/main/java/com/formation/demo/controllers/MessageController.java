package com.formation.demo.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.data.mongodb.repository.Update;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.formation.demo.entities.Fichiers;
import com.formation.demo.entities.Message;
import com.formation.demo.entities.Seance;
import com.formation.demo.repository.FichierRepo;
import com.formation.demo.repository.MessageRepository;
import com.formation.demo.services.MessageService;
import com.formation.demo.services.SupaBaseService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageRepository messageRepository;

    private final MessageService messageService;
    private final SupaBaseService supaBaseService;

    private final FichierRepo fichierRepo;

    @PostMapping
    public ResponseEntity<Message> saveMessage(@RequestBody Message message) {
        return messageService.createMessage(message);
    }

    @GetMapping("")
    public ResponseEntity<Object> getAllMessages() {
        return ResponseEntity.ok(messageService.allMessage());
    }

    @GetMapping("/promotion")
    public ResponseEntity<Object> getMessageByPromotion(@RequestParam String promotionId) {
        return ResponseEntity.ok(messageService.findMessageByPromotion(promotionId));
    }

    @GetMapping("/{idGroupe}")
    public ResponseEntity<List<Message>> getMethodName(@PathVariable("idGroupe") String id) {
        return ResponseEntity.ok(messageRepository.findByGroupe(id));
    }

    @PutMapping("")
    public ResponseEntity<Object> updateMessage(@RequestParam String id, @RequestParam String content) {
        return messageService.modifierMessage(id, content);
    }

    @PostMapping("/attachment")
    public ResponseEntity<?> addFileOnSeance(@RequestPart("file") MultipartFile file,
            @RequestPart("message") String messageJson) {
        try {

            ObjectMapper objectMapper = new ObjectMapper();
            Message message = objectMapper.readValue(messageJson, Message.class);

            Map<String, Object> result = supaBaseService.uploadFile(file);

            Fichiers fichiers = new Fichiers();

            fichiers.setName(result.get("name").toString());
            fichiers.setType(result.get("type").toString());
            fichiers.setUrl(result.get("url").toString());
            Fichiers fichier = fichierRepo.save(fichiers);

            message.setFichiers(fichier);
            System.out.println("message avec fichier: " + message);
            return ResponseEntity.ok().body(messageService.createMessage(message));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erreur d'ajout du fichier");
        }
    }

}
