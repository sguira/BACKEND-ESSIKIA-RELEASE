package com.formation.demo.services;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;

import com.formation.demo.dto.CodeReductionDTO;
import com.formation.demo.dto.CodeReductionValidationRequest;
import com.formation.demo.dto.CodeReductionValidationResponse;
import com.formation.demo.entities.CodeReduction;
import com.formation.demo.enumeration.TypeReduction;
import com.formation.demo.repository.CodeReductionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CodeReductionService {

    private final CodeReductionRepository codeReductionRepository;

    public List<CodeReduction> getAll() {
        return codeReductionRepository.findAll();
    }

    public CodeReduction create(CodeReductionDTO dto) {
        CodeReduction code = CodeReduction.builder()
                .code(dto.getCode().trim().toUpperCase())
                .typeReduction(dto.getTypeReduction())
                .valeur(dto.getValeur())
                .offreId(dto.getOffreId())
                .dateExpiration(dto.getDateExpiration())
                .nombreUtilisationsMax(dto.getNombreUtilisationsMax())
                .nombreUtilisations(0)
                .isActive(dto.isActive())
                .build();
        return codeReductionRepository.save(code);
    }

    public CodeReduction update(String id, CodeReductionDTO dto) {
        CodeReduction existing = codeReductionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Code non trouvé: " + id));
        existing.setCode(dto.getCode().trim().toUpperCase());
        existing.setTypeReduction(dto.getTypeReduction());
        existing.setValeur(dto.getValeur());
        existing.setOffreId(dto.getOffreId());
        existing.setDateExpiration(dto.getDateExpiration());
        existing.setNombreUtilisationsMax(dto.getNombreUtilisationsMax());
        existing.setActive(dto.isActive());
        return codeReductionRepository.save(existing);
    }

    public void delete(String id) {
        codeReductionRepository.deleteById(id);
    }

    public CodeReductionValidationResponse validate(CodeReductionValidationRequest request) {
        CodeReduction code = codeReductionRepository
                .findByCodeIgnoreCase(request.getCode().trim())
                .orElse(null);

        if (code == null) {
            return CodeReductionValidationResponse.builder()
                    .valid(false)
                    .message("Code de réduction invalide")
                    .build();
        }

        if (!code.isActive()) {
            return CodeReductionValidationResponse.builder()
                    .valid(false)
                    .message("Ce code de réduction est désactivé")
                    .build();
        }

        if (code.getDateExpiration() != null && Instant.now().isAfter(code.getDateExpiration())) {
            return CodeReductionValidationResponse.builder()
                    .valid(false)
                    .message("Ce code de réduction a expiré")
                    .build();
        }

        if (code.getNombreUtilisationsMax() > 0
                && code.getNombreUtilisations() >= code.getNombreUtilisationsMax()) {
            return CodeReductionValidationResponse.builder()
                    .valid(false)
                    .message("Ce code a atteint sa limite d'utilisation")
                    .build();
        }

        if (code.getOffreId() != null && !code.getOffreId().equals(request.getOffreId())) {
            return CodeReductionValidationResponse.builder()
                    .valid(false)
                    .message("Ce code n'est pas valable pour cette offre")
                    .build();
        }

        double montantApres;
        if (code.getTypeReduction() == TypeReduction.POURCENTAGE) {
            double reduction = request.getMontantOriginal() * code.getValeur() / 100.0;
            montantApres = request.getMontantOriginal() - reduction;
        } else {
            montantApres = Math.max(0, request.getMontantOriginal() - code.getValeur());
        }
        montantApres = Math.round(montantApres * 100.0) / 100.0;

        return CodeReductionValidationResponse.builder()
                .valid(true)
                .message("Code valide")
                .codeId(code.getId())
                .typeReduction(code.getTypeReduction())
                .valeur(code.getValeur())
                .montantOriginal(request.getMontantOriginal())
                .montantApresReduction(montantApres)
                .build();
    }

    public void incrementerUtilisations(String codeId) {
        codeReductionRepository.findById(codeId).ifPresent(code -> {
            code.setNombreUtilisations(code.getNombreUtilisations() + 1);
            codeReductionRepository.save(code);
        });
    }
}
