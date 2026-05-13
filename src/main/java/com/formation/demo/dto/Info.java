package com.formation.demo.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Info {
    private String objectif;
    private String qualite;
    private String defauts;
    private String profession;

}
