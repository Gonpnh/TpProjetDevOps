package ytg.projetjavaytg.Services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ytg.projetjavaytg.Models.Evaluation;
import ytg.projetjavaytg.Repositories.EvaluationRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class EvaluationService {

    private final EvaluationRepository evaluationRepository;

    public EvaluationService(EvaluationRepository evaluationRepository) {
        this.evaluationRepository = evaluationRepository;
    }

    public List<Evaluation> getAllEvaluations() {
        return evaluationRepository.findAll();
    }

    public Optional<Evaluation> getEvaluationById(Long id) {
        return evaluationRepository.findById(id);
    }

    @Transactional
    public Evaluation createEvaluation(Evaluation evaluation) {
        evaluation.setDateCreation(Instant.now());
        evaluation.setDateModification(Instant.now());
        return evaluationRepository.save(evaluation);
    }
}
