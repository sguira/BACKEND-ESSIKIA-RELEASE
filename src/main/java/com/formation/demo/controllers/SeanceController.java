package com.formation.demo.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.formation.demo.entities.Fichiers;
import com.formation.demo.entities.Seance;
import com.formation.demo.entities.VideoExplicative;
import com.formation.demo.repository.FichierRepo;
import com.formation.demo.repository.SeanceRepository;
import com.formation.demo.services.FichierService;
import com.formation.demo.services.FileService;
import com.formation.demo.services.R2Service;
import com.formation.demo.services.SupaBaseService;
import com.formation.demo.services.VideoExplicativeService;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("api/v1/seance")
@CrossOrigin("*")
@RequiredArgsConstructor
public class SeanceController {

    private final SeanceRepository seanceRepository;
    private final SupaBaseService supaBaseService;
    private final FichierRepo fichierRepo;
    private final VideoExplicativeService videoExplicativeService;
    private final FichierService fichierService;
    private final R2Service r2Service;

    @PostMapping("/add")
    public ResponseEntity<Seance> saveSeance(@RequestBody Seance seance) {
        Seance existingSeance = seanceRepository.findById(seance.getId()).orElse(null);
        if (existingSeance != null) {
            existingSeance.setContent(seance.getContent());
            return ResponseEntity.ok().body(seanceRepository.save(existingSeance));
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/add-file/{id}")
    public ResponseEntity<?> addFileOnSeance(@PathVariable String id, @RequestBody Fichiers fichiers) {
        try {
            Seance seance = seanceRepository.findById(id).orElse(null);
            if (seance == null) {
                return ResponseEntity.status(404).body("Aucune seance trouvée");
            }
            seance.ajouterFichier(fichiers);
            seanceRepository.save(seance);
            return ResponseEntity.ok().body(fichiers);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erreur d'ajout du fichier");
        }
    }

    @GetMapping("/liste-seance/{id}")
    public ResponseEntity seanceBySubject(@PathVariable String id) {
        List<Seance> seances = new ArrayList<>();
        for (Seance seance : seanceRepository.findAll()) {
            if (seance.getFormation() != null) {
                if (seance.getFormation().getId() != null && seance.getFormation().getId().equals(id)) {
                    seances.add(seance);
                }
            }
        }
        return ResponseEntity.ok().body(seances);
    }

    @GetMapping("/video-explicative")
    ResponseEntity<VideoExplicative> getVideoExplicativeBySeanceId(@RequestParam String seanceId,
            @RequestParam String promotionId) {
        System.out.println("Fetching VideoExplicative for seanceId: " + seanceId + " and promotionId: " + promotionId);
        VideoExplicative video = videoExplicativeService.getVideoExplicativeBySeanceIdAndPromotionId(seanceId,
                promotionId);
        if (video != null) {
            return ResponseEntity.ok(video);
        } else {
            System.out.println("Aucune vidéo explicative trouvée pour la séanceId: " + seanceId + " et promotionId: "
                    + promotionId);
            return ResponseEntity.status(HttpStatusCode.valueOf(400)).build();
        }

    }

    // ajout de video quickoff
    @PostMapping("/add-video-quickoff/{seanceId}")
    public ResponseEntity<?> addVideoQuickOffToSeance(@RequestBody Fichiers videoQuickOff,
            @PathVariable String seanceId) {
        try {

            Seance seance = seanceRepository.findById(seanceId).orElse(null);
            if (seance == null) {
                return ResponseEntity.status(404).body("Aucune seance trouvée");
            }
            seance.setVideoQuickOff(videoQuickOff);
            seance = seanceRepository.save(seance);
            return ResponseEntity.ok().body(seance);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erreur d'ajout de la vidéo QuickOff");
        }
    }

    @PostMapping("/delete-file/{seanceId}")
    public ResponseEntity<?> deleteFileFromSeance(@PathVariable String seanceId, @RequestBody Fichiers fichiers) {
        try {
            Seance seance = seanceRepository.findById(seanceId).orElse(null);
            if (seance == null) {
                return ResponseEntity.status(404).body("Aucune seance trouvée");
            }
            System.out.println("Suppression du fichier: " + fichiers.getId() + " de la séance: " + seanceId);
            // fichierService.deleteFile(fichiers.getId());
            System.out.println("Suppression du fichier de R2 avec URL: " + fichiers.getUrl());
            r2Service.deleteFile(fichiers.getUrl());
            seance.deleteFileByid(fichiers.getId());

            System.out.println("Fichier supprimé de la séance: " + fichiers.getId());
            seanceRepository.save(seance);
            return ResponseEntity.ok().body(fichiers);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erreur de suppression du fichier");
        }
    }

    @DeleteMapping("/{seanceId}")
    public ResponseEntity<?> deleteSeance(@PathVariable String seanceId) {
        try {
            Seance seance = seanceRepository.findById(seanceId).orElse(null);
            if (seance == null) {
                return ResponseEntity.status(404).body("Aucune seance trouvée");
            }
            seanceRepository.delete(seance);
            return ResponseEntity.ok().body("Séance supprimée avec succès");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erreur de suppression de la séance");
        }

    }

    @PostMapping("/delete-video-quickoff")
    public ResponseEntity<?> deleteVideoQuickOffFromSeance(@RequestParam String seanceId,
            @RequestParam String fileKey) {
        try {
            Seance seance = seanceRepository.findById(seanceId).orElse(null);
            if (seance == null) {
                return ResponseEntity.status(404).body("Aucune seance trouvée");
            }
            Fichiers videoQuickOff = seance.getVideoQuickOff();
            if (videoQuickOff == null) {
                return ResponseEntity.status(404).body("Aucune vidéo QuickOff associée à cette séance");
            }
            r2Service.deleteFile(fileKey);
            seance.setVideoQuickOff(null);
            seanceRepository.save(seance);
            return ResponseEntity.ok().body("Vidéo QuickOff supprimée de la séance avec succès");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erreur de suppression de la vidéo QuickOff de la séance");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Seance> getSeanceById(@PathVariable String id) {
        Seance seance = seanceRepository.findById(id).orElse(null);
        if (seance != null) {
            return ResponseEntity.ok(seance);
        } else {
            return ResponseEntity.status(404).build();
        }
    }

}