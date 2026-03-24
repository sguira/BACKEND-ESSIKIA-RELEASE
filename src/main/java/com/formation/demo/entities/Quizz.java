package com.formation.demo.entities;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.ToString;

@Document
@Data
@ToString
public class Quizz {

    @Id
    private String id;
    private String description;
    private String title;
    private int minimumScore;
    private int durationInMinutes;
    private boolean isActive;
    private String moduleId;
    private String promotionId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime staredAt;

    private String seanceId;
    List<Question> questions;

}
