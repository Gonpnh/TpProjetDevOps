package ytg.projetjavaytg.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ytg.projetjavaytg.Models.AnneeAcademique;

import java.util.Optional;

public interface AnneeAcademiqueRepository extends JpaRepository<AnneeAcademique, Long> {
    Optional<AnneeAcademique> findByActiveTrue();
}

