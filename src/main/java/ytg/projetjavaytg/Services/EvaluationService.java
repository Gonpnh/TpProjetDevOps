package ytg.projetjavaytg.Services;

import org.springframework.stereotype.Service;
import ytg.projetjavaytg.Repositories.EvaluationRepository;

@Service
public class EvaluationService {

    private final EvaluationRepository evaluationRepository;

    public EvaluationService(EvaluationRepository evaluationRepository) {
        this.evaluationRepository = evaluationRepository;
    }
}
