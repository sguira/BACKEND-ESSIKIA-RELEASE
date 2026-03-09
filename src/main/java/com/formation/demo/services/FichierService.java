package com.formation.demo.services;

import org.springframework.stereotype.Service;

import com.formation.demo.repository.FichierRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FichierService {

    private final FichierRepo fichierRepo;

    public void deleteFile(String id) {
        fichierRepo.deleteById(id);
    }

}
