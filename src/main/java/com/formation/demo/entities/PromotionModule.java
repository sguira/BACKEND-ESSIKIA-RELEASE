package com.formation.demo.entities;

import org.springframework.data.mongodb.core.mapping.DBRef;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PromotionModule {

    @DBRef
    private Modules module;

    @DBRef
    private Formateur formateur;
}
