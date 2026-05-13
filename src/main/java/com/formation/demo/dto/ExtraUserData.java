package com.formation.demo.dto;

import com.formation.demo.entities.Fichiers;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ExtraUserData {
    private Info info;
    private Fichiers avatar;
}
