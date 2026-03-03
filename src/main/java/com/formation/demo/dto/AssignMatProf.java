package com.formation.demo.dto;

import java.io.Serializable;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class AssignMatProf implements Serializable {

    private String idMatiere;
    private String idFormateur;

}
