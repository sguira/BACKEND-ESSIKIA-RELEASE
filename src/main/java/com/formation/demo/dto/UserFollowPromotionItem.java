package com.formation.demo.dto;

import com.formation.demo.entities.Promotion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class UserFollowPromotionItem {

    private Promotion promotion;
    private int nbSeance = 0;
    private int nbSeanceComplete = 0;

}
