package com.formation.demo.controllers;

import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import com.formation.demo.repository.FichierRepo;
import com.formation.demo.repository.MessageRepository;
import com.formation.demo.services.MessageService;
import com.formation.demo.services.R2Service;
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
    private final R2Service r2Service;

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
    public ResponseEntity<?> addFileOnSeance(@RequestBody Message message) {
        try {

            // ObjectMapper objectMapper = new ObjectMapper();
            // Message message = objectMapper.readValue(messageJson, Message.class);
            // Map<String, Object> result = supaBaseService.uploadFile(file);
            Fichiers fichiers = new Fichiers();
            if (message.getFichiers() != null) {
                fichiers.setName(message.getFichiers().getName());
                fichiers.setType(message.getFichiers().getType());
                fichiers.setUrl(message.getFichiers().getUrl());
            }

            Fichiers fichier = fichierRepo.save(fichiers);
            message.setFichiers(fichier);
            System.out.println("message avec fichier: " + message);
            return ResponseEntity.ok().body(messageService.createMessage(message));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erreur d'ajout du fichier");
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteMessage(@PathVariable String id) {
        try {
            Message message = messageRepository.findById(id).orElse(null);
            if (message == null) {
                return ResponseEntity.status(404).body("Aucun message trouvé");
            }

            if (message.getFichiers() == null) {
                System.out.println("Message sans fichier, suppression directe");
                // messageRepository.deleteById(id);
                // return ResponseEntity.ok().body("Message supprimé avec succès");
            }
            if (message.getFichiers() != null) {
                r2Service.deleteFile(message.getFichiers().getName());
                // fichierRepo.deleteById(message.getFichiers().getId());
            }
            messageRepository.deleteById(id);
            return ResponseEntity.ok().body("Message supprimé avec succès");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erreur de suppression du message");
        }
    }

}
