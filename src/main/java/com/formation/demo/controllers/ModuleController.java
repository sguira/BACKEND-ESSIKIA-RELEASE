package com.formation.demo.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.formation.demo.entities.Matiere;
import com.formation.demo.entities.Modules;
import com.formation.demo.entities.Seance;
import com.formation.demo.repository.SeanceRepository;
import com.formation.demo.services.MatiereService;
import com.formation.demo.services.ModuleService;

// import io.swagger.v3.oas.annotations.parameters.RequestBody;m::!<
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/module")
public class ModuleController {

    private final ModuleService moduleService;

    private final SeanceRepository seanceRepository;

    @PostMapping("")
    public ResponseEntity<Object> addModule(@RequestBody Modules modules) {
        System.out.print("Nouveau Module");
        return moduleService.createModule(modules);
    }

    @GetMapping("")
    public ResponseEntity<List<Modules>> modules() {
        return moduleService.allModule();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Modules> _getModuleById(@PathVariable String id) {
        return moduleService.getModuleById(id);
    }

    @GetMapping("/list-matiere")
    public ResponseEntity<Object> listMatiere(
            @RequestParam("moduleId") String moduleId) {
        return moduleService.listeMatiereByModule(moduleId);
    }

    @GetMapping("/categorie")
    public ResponseEntity<Object> getMobuleByCategory(@RequestParam(name = "categorie") String categorie) {
        return moduleService.listModuleByCategory(categorie);
    }

    @GetMapping("/student/{id}")
    public ResponseEntity<Object> getModuleById(@PathVariable String id) {
        return moduleService.listeModuleByEtudiant(id);
    }

    @GetMapping("/seance/{id}")
    public ResponseEntity<List<Seance>> getSeancesByModule(@PathVariable String id) {
        return ResponseEntity.ok(moduleService.getSeancesByModule(id));
    }

    @PostMapping("add-seance/{moduleId}")
    ResponseEntity<Object> _addSeanceToModule(@PathVariable String moduleId, @RequestBody Seance seance) {
        return moduleService.addSeanceToModule(moduleId, seance);
    }

    @DeleteMapping("{moduleId}")
    public ResponseEntity<Object> deleteModule(@PathVariable String moduleId) {
        return moduleService.deleteModule(moduleId);
    }

    @DeleteMapping("/remove-seance/{moduleId}")
    public ResponseEntity<Object> removeSeanceFromModule(@PathVariable String moduleId, @RequestBody Seance seance) {
        moduleService.deleteSeanceFromModule(moduleId, seance.getId());
        return ResponseEntity.ok().body("Séance supprimée du module avec succès");
    }

    @PostMapping("/make-not-free/{moduleId}")
    public ResponseEntity<Object> makeModuleNotFree(@PathVariable String moduleId) {
        System.out.println("Module ID: " + moduleId);
        moduleService.makeNotFree(moduleId);
        return ResponseEntity.ok().body("Module marqué comme non gratuit avec succès");
    }

    @PostMapping("/make-free/{moduleId}")
    public ResponseEntity<Object> makeModuleFree(@PathVariable String moduleId) {
        System.out.println("Module ID: " + moduleId);
        moduleService.makeFree(moduleId);
        return ResponseEntity.ok().body("Module marqué comme gratuit avec succès");
    }

}