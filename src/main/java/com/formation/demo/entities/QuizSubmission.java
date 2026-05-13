package com.formation.demo.entities;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuizSubmission {

    @Id
    private String id;
    private String quizzId;
    private String userId;
    private int score;
    private boolean isPassed;
    private LocalDateTime startedAt;
    private LocalDateTime submittedAt;
    private List<Answer> answers;

}
