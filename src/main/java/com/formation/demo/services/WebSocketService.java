package com.formation.demo.services;

import java.util.Map;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WebSocketService {

    private final SimpMessagingTemplate messagingTemplate;

    public void sendMessage(String destination, String message) {
        messagingTemplate.convertAndSend(destination, message);
    }

    public void sendToUser(String user, String destination, String message) {
        messagingTemplate.convertAndSendToUser(user, destination, message);
    }

    public void notifyAll(String event, Object payload) {
        messagingTemplate.convertAndSend(
                "/topic/events",
                Map.of(
                        "event", event,
                        "data", payload,
                        "timestamp", System.currentTimeMillis()));
    }

}
