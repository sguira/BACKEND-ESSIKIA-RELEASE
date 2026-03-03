package com.formation.demo.services;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.formation.demo.entities.Seance;
import com.formation.demo.repository.SeanceRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Component
public class SeanceService {

    private final SeanceRepository seanceRepository;

    Seance getSeanceById(String id) {
        Seance seance = seanceRepository.findById(id).orElse(null);
        return seance;
    }

    List<Seance> getSeancesByModule(String moduleId) {
        return seanceRepository.findByModuleId(moduleId);
    }

}
