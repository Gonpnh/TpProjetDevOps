package ytg.projetjavaytg.Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "apprenti")
public class Apprenti {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "nom", nullable = false, length = 100)
    private String nom;

    @Column(name = "prenom", nullable = false, length = 100)
    private String prenom;

    @Column(name = "email", nullable = false, length = 150)
    private String email;

    @Column(name = "telephone", length = 20)
    private String telephone;

    @Column(name = "programme", length = 100)
    private String programme;

    @Column(name = "annee_academique", nullable = false, length = 20)
    private String anneeAcademique;

    @Column(name = "majeure", length = 150)
    private String majeure;

    @ColumnDefault("'I1'")
    @Column(name = "niveau", length = 10)
    private String niveau;

    @ColumnDefault("0")
    @Column(name = "archive")
    private Boolean archive;

    @Lob
    @Column(name = "mission_mots_cles")
    private String missionMotsCles;

    @Column(name = "mission_metier_cible", length = 200)
    private String missionMetierCible;

    @Lob
    @Column(name = "mission_commentaires")
    private String missionCommentaires;

    @Lob
    @Column(name = "feedback_tuteur")
    private String feedbackTuteur;

    @Lob
    @Column(name = "remarques_generales")
    private String remarquesGenerales;

    @ColumnDefault("current_timestamp()")
    @Column(name = "date_creation", nullable = false)
    private Instant dateCreation;

    @ColumnDefault("current_timestamp()")
    @Column(name = "date_modification", nullable = false)
    private Instant dateModification;

}