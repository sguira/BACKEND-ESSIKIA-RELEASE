package com.formation.demo.entities;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

// import jakarta.persistence.Id;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Document
@Data
@RequiredArgsConstructor
public class Groupe {
    @Id
    private String id;
    private String name;
    private String descriptions;

    @DBRef
    private Promotion promotion;

    @DBRef
    List<Message> messages = new ArrayList<>();
}
