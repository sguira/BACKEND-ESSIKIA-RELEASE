package com.formation.demo.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

public class RecupP implements Serializable {
    private String email;
    private String newPassword;
    private String code;

}
