package com.formation.demo.dto;

import java.io.Serializable;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class PromotionSuscriptionDTO implements Serializable {
    private String utilisateurId;
    private String promotionId;

}
