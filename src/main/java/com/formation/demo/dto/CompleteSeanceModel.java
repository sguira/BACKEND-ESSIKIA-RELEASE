package com.formation.demo.dto;

import java.io.Serializable;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CompleteSeanceModel implements Serializable {
    private String moduleId;
    private String seanceId;
    private String userId;

}
