package com.formation.demo.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.formation.demo.entities.QuizSubmission;
import com.formation.demo.entities.Quizz;
import com.formation.demo.repository.QuizzRepository;
import com.formation.demo.services.QuizzService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/api/v1/quizz")
@RequiredArgsConstructor
public class QuizzController {

    private final QuizzService quizzService;

    @PostMapping("/create")
    public ResponseEntity createQuizz(@RequestBody Quizz quizz) {
        try {
            return ResponseEntity.ok(quizzService.createQuizz(quizz));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/seance/{seanceId}")
    public ResponseEntity<?> getQuizzBySeanceAndPromotion(@PathVariable String seanceId) {
        try {
            return ResponseEntity.ok(quizzService.getQuizzBySeanceId(seanceId));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/start/{userId}/{quizzId}")
    public ResponseEntity<?> startQuizz(@PathVariable String userId, @PathVariable String quizzId) {
        try {
            quizzService.startQuizz(quizzId, userId);
            return ResponseEntity.ok("Quizz started successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/submit/{userId}/{quizzId}")
    public ResponseEntity<?> submitQuizz(@PathVariable String userId, @PathVariable String quizzId,
            @RequestBody QuizSubmission answers) {
        try {
            return ResponseEntity.ok(quizzService.submitQuiz(quizzId, userId, answers));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/hasSubmitted/{userId}/{seanceId}")
    public ResponseEntity<?> hasUserSubmittedQuizz(@PathVariable String userId, @PathVariable String seanceId) {
        try {
            boolean hasSubmitted = quizzService.hasUserSubmittedQuizz(seanceId, userId);
            return ResponseEntity.ok(hasSubmitted);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/score/{userId}/{quizzId}")
    public ResponseEntity<?> getUserScore(@PathVariable String userId, @PathVariable String quizzId) {
        try {
            QuizSubmission submission = quizzService.getUserQuizSubmission(quizzId, userId);
            if (submission != null) {
                Map<String, Object> results = new HashMap<>();
                results.put("score", submission.getScore());
                results.put("passed", submission.isPassed());
                System.out.println("User " + userId + " scored " + submission.getScore() + " on quiz " + quizzId);
                return ResponseEntity.ok(results);
            } else {
                System.out.println("No submission found for user " + userId + " on quiz " + quizzId);
                return ResponseEntity.ok("No submission found for this user and quiz.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
