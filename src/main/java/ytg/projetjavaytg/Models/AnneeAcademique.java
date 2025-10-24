package ytg.projetjavaytg.Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "annee_academique")
public class AnneeAcademique {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "annee", nullable = false, unique = true, length = 20)
    private String annee;  // Format: "2025-2026"

    @Column(name = "active", nullable = false)
    private Boolean active = false;  // Une seule année peut être active

    @Column(name = "date_creation", nullable = false)
    private Instant dateCreation;

    @Column(name = "date_activation")
    private Instant dateActivation;
}

