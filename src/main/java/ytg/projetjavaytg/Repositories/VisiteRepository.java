package ytg.projetjavaytg.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ytg.projetjavaytg.Models.Visite;

import java.util.List;

public interface VisiteRepository extends JpaRepository<Visite,Long> {
    List<Visite> findByApprentiIdOrderByDateVisiteDesc(Long apprentiId);
}
