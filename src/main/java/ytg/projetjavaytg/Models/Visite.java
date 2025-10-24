package ytg.projetjavaytg.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "visite")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Visite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "apprenti_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Apprenti apprenti;

    @Column(name = "date_visite")
    private LocalDate dateVisite;

    @Column(name = "format", length = 20)
    private String format;

    @Lob
    @Column(name = "commentaires")
    private String commentaires;

    @ColumnDefault("current_timestamp()")
    @Column(name = "date_creation", nullable = false)
    private Instant dateCreation;

}