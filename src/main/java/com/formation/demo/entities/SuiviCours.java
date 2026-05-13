package com.formation.demo.entities;

import java.time.Instant;
import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Document(collection = "suivi_cours")
@CompoundIndexes({
        @CompoundIndex(name = "idx_suivi_user_completed", def = "{'utilisateur': 1, 'isCompleted': 1}"),
        @CompoundIndex(name = "idx_suivi_user_module_completed", def = "{'utilisateur': 1, 'module': 1, 'isCompleted': 1}")
})
@AllArgsConstructor
@NoArgsConstructor
public class SuiviCours {

    @Id
    private String id;
    @DBRef
    @Indexed
    private Modules module;
    @DBRef
    @Indexed
    private Utilisateur utilisateur;
    @Indexed
    private boolean isCompleted;
    @DBRef
    private Seance currentSeance;
    private int currentSeanceIndex;
    private Instant createAt;

}
