package com.formation.demo.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SuscriptionDTO {

    @NotNull
    private String email;
    @NotNull
    private String offreId;
    @NotNull
    private String paiementIntent;

}
