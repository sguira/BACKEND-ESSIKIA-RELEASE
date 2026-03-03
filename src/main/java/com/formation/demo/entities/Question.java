package com.formation.demo.entities;

import java.util.List;

import com.formation.demo.enumeration.QuestionType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Question {

    private String id;
    private String title;
    private String type; // e.g., "multiple-choice", "true-false", etc
    private int score;
    private QuestionType questionType;
    List<Options> options;

}
