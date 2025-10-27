package ytg.projetjavaytg.Services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ytg.projetjavaytg.Models.Evaluation;
import ytg.projetjavaytg.Repositories.EvaluationRepository;
import ytg.projetjavaytg.exception.ResourceNotFoundException;

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
        List<Evaluation> evaluations = evaluationRepository.findAll();
        if (evaluations.isEmpty()){
            throw new ResourceNotFoundException("Aucune evaluation en base");
        }
        return evaluations;
    }

    public Optional<Evaluation> getEvaluationById(Long id) {
        Optional<Evaluation> evaluation = evaluationRepository.findById(id);
        if (evaluation.isPresent()){
            return evaluation;
        }
        throw new ResourceNotFoundException("Aucune evaluation trouver avec id " + id);
    }

    @Transactional
    public Evaluation createEvaluation(Evaluation evaluation) {
        evaluation.setDateCreation(Instant.now());
        evaluation.setDateModification(Instant.now());
        return evaluationRepository.save(evaluation);
    }
}
