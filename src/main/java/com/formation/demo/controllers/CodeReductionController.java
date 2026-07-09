package com.formation.demo.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.formation.demo.dto.CodeReductionDTO;
import com.formation.demo.dto.CodeReductionValidationRequest;
import com.formation.demo.dto.CodeReductionValidationResponse;
import com.formation.demo.entities.CodeReduction;
import com.formation.demo.services.CodeReductionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/codes-reduction")
@RequiredArgsConstructor
public class CodeReductionController {

    private final CodeReductionService codeReductionService;

    @GetMapping
    public ResponseEntity<List<CodeReduction>> getAll() {
        return ResponseEntity.ok(codeReductionService.getAll());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CodeReductionDTO dto) {
        try {
            return ResponseEntity.ok(codeReductionService.create(dto));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody CodeReductionDTO dto) {
        try {
            return ResponseEntity.ok(codeReductionService.update(id, dto));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        codeReductionService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/validate")
    public ResponseEntity<CodeReductionValidationResponse> validate(
            @RequestBody CodeReductionValidationRequest request) {
        return ResponseEntity.ok(codeReductionService.validate(request));
    }
}
