package ytg.projetjavaytg.Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "evaluation")
public class Evaluation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "apprenti_id", nullable = false)
    private Apprenti apprenti;

    @Column(name = "memoire_theme")
    private String memoireTheme;

    @Column(name = "memoire_note", precision = 4, scale = 2)
    private BigDecimal memoireNote;

    @Lob
    @Column(name = "memoire_commentaires")
    private String memoireCommentaires;

    @Column(name = "soutenance_date")
    private LocalDate soutenanceDate;

    @Column(name = "soutenance_note", precision = 4, scale = 2)
    private BigDecimal soutenanceNote;

    @Lob
    @Column(name = "soutenance_commentaires")
    private String soutenanceCommentaires;

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