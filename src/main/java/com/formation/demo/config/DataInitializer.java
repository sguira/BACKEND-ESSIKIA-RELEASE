package com.formation.demo.config;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.formation.demo.entities.Offre;
import com.formation.demo.entities.OffreOption;
import com.formation.demo.enumeration.TypeOffre;
import com.formation.demo.repository.OffreOptionRepo;
import com.formation.demo.repository.OffreRepo;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

        private final OffreRepo offreRepo;
        private final OffreOptionRepo offreOptionRepo;

        @Bean
        CommandLineRunner initDatabase() {
                return args -> {
                        if (offreRepo.count() == 0) {
                                // 1. Accès Libre
                                List<OffreOption> optionsLibre = List.of(
                                                new OffreOption(null, "Cours écrits", "Accès aux cours écrits", true),
                                                new OffreOption(null, "Vidéos", "Vidéos explicatives", false),
                                                new OffreOption(null, "Workbooks / Templates", "Accès aux supports",
                                                                false),
                                                new OffreOption(null, "Lives experts", "Accès aux lives", false),
                                                new OffreOption(null, "Communauté", "Accès à la communauté", false),
                                                new OffreOption(null, "Replays", "Replays des sessions", false));
                                offreOptionRepo.saveAll(optionsLibre);

                                offreRepo.save(Offre.builder()
                                                .nom("Accès Libre")
                                                .description("Accès gratuit aux modules et cours de base.")
                                                .typeOffre(TypeOffre.ACCES_LIBRE)
                                                .prix(0)
                                                .nombrePaiements(1)
                                                .dureeEnMois(0)
                                                .isActive(true)
                                                .options(optionsLibre)
                                                .build());

                                // 2. Programme ESSIKIA
                                List<OffreOption> optionsEssikia = List.of(
                                                new OffreOption(null, "Cours écrits", "Accès aux cours écrits", true),
                                                new OffreOption(null, "Vidéos", "Vidéos explicatives", true),
                                                new OffreOption(null, "Workbooks / Templates", "Accès aux supports",
                                                                true),
                                                new OffreOption(null, "Lives experts", "Accès aux lives", false),
                                                new OffreOption(null, "Communauté", "Accès à la communauté", false),
                                                new OffreOption(null, "Replays", "Replays des sessions", false));
                                offreOptionRepo.saveAll(optionsEssikia);

                                offreRepo.save(Offre.builder()
                                                .nom("Programme ESSIKIA")
                                                .description("Programme complet pour maîtriser les fondamentaux.")
                                                .typeOffre(TypeOffre.PROGRAMME_ESSIKIA)
                                                .prix(79)
                                                .nombrePaiements(1)
                                                .dureeEnMois(12)
                                                .privilege(2)
                                                .isActive(true)
                                                .options(optionsEssikia)
                                                .build());

                                // 3. Programme + Communauté
                                List<OffreOption> optionsCommunaute = List.of(
                                                new OffreOption(null, "Cours écrits", "Accès aux cours écrits", true),
                                                new OffreOption(null, "Vidéos", "Vidéos explicatives", true),
                                                new OffreOption(null, "Workbooks / Templates", "Accès aux supports",
                                                                true),
                                                new OffreOption(null, "Lives experts", "Accès aux lives", true),
                                                new OffreOption(null, "Communauté", "Accès à la communauté", true),
                                                new OffreOption(null, "Replays", "Replays des sessions", true));
                                offreOptionRepo.saveAll(optionsCommunaute);

                                offreRepo.save(Offre.builder()
                                                .nom("Programme + Communauté")
                                                .description("Le programme complet avec accès à la communauté et aux lives.")
                                                .typeOffre(TypeOffre.PROGRAMME_COMMUNAUTE)
                                                .prix(149)
                                                .isPopulaire(true)
                                                .nombrePaiements(1)
                                                .dureeEnMois(12)
                                                .isActive(true)
                                                .privilege(3)
                                                .options(optionsCommunaute)
                                                .build());

                                // 4. Expérience Premium
                                List<OffreOption> optionsPremium = List.of(
                                                new OffreOption(null, "Cours écrits", "Accès aux cours écrits", true),
                                                new OffreOption(null, "Vidéos", "Vidéos explicatives", true),
                                                new OffreOption(null, "Workbooks / Templates", "Accès aux supports",
                                                                true),
                                                new OffreOption(null, "Lives experts", "Accès aux lives", true),
                                                new OffreOption(null, "Communauté", "Accès à la communauté", true),
                                                new OffreOption(null, "Replays + Coaching",
                                                                "Replays et coaching personnalisé", true));
                                offreOptionRepo.saveAll(optionsPremium);

                                offreRepo.save(Offre.builder()
                                                .nom("Expérience Premium")
                                                .description("Accompagnement personnalisé et coaching intensif.")
                                                .typeOffre(TypeOffre.EXPERIENCE_PREMIUM)
                                                .prix(249)
                                                .nombrePaiements(1)
                                                .dureeEnMois(12)
                                                .privilege(4)
                                                .isActive(true)
                                                .options(optionsPremium)
                                                .build());

                                System.out.println("Base de données des offres initialisée avec succès !");
                        }
                };
        }
}
