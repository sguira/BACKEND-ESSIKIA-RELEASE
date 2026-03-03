package com.formation.demo.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.formation.demo.entities.Promotion;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class PromotionDTO implements Serializable {

    // private String name;
    private Promotion promotion;
    private Module module;
    // private List<AssignMatProf> matieres = new ArrayList<>();
}
