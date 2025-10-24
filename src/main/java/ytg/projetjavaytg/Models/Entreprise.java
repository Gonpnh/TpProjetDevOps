package ytg.projetjavaytg.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "entreprise")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Entreprise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "raison_sociale", nullable = false, length = 200)
    private String raisonSociale;

    @Lob
    @Column(name = "adresse")
    private String adresse;

    @Lob
    @Column(name = "informations_acces")
    private String informationsAcces;

}