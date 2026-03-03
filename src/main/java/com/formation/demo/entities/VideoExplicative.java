package com.formation.demo.entities;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoExplicative {

    @Id
    private String id;
    private String title;
    private String description;
    private String seanceId;
    private String promotionId;
    private String formateurId;
    private Fichiers files;

}
