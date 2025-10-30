package ytg.projetjavaytg.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ytg.projetjavaytg.Models.Visite;

import java.util.List;
import java.util.Optional;

public interface VisiteRepository extends JpaRepository<Visite,Long> {
    List<Visite> findByApprentiIdOrderByDateVisiteDesc(Long apprentiId);
    Optional<Void> deleteByApprentiId(Long apprentiId);
}
