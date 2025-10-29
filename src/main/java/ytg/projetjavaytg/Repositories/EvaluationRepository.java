package ytg.projetjavaytg.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ytg.projetjavaytg.Models.Evaluation;

import java.util.Optional;

public interface EvaluationRepository extends JpaRepository<Evaluation,Long> {
    Optional<Evaluation> findByApprentiId(Long apprentiId);
}
