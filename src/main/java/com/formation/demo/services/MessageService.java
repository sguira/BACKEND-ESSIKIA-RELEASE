package com.formation.demo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.formation.demo.entities.Message;
import com.formation.demo.repository.MessageRepository;

import lombok.RequiredArgsConstructor;

@Service
@Component
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    public ResponseEntity createMessage(Message message) {
        try {
            Message saveMessage = messageRepository.save(message);
            simpMessagingTemplate.convertAndSend("/topic/group" + saveMessage.getGroupe(), saveMessage);
            return ResponseEntity.ok(saveMessage);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    public ResponseEntity<Object> deleteMessage(String id) {
        try {
            messageRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    public ResponseEntity<Object> allMessage() {
        try {
            return ResponseEntity.ok(messageRepository.findAll());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    public ResponseEntity<List<Message>> findMessageByPromotion(String promotionId) {
        try {
            return ResponseEntity.ok(messageRepository.findByPromotion(promotionId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    public ResponseEntity<Object> modifierMessage(String id, String content) {
        try {
            Message mes = messageRepository.findById(id).get();
            mes.setContent(content);
            return ResponseEntity.ok(messageRepository.save(mes));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

}
