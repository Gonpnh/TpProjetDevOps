package ytg.projetjavaytg.Controllers.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ytg.projetjavaytg.Repositories.EvaluationRepository;
import ytg.projetjavaytg.Services.EvaluationService;

@RestController
@RequestMapping("/api/evaluation")
public class EvaluationController {

    private final EvaluationService evaluationService;

    public EvaluationController(EvaluationService evaluationService) {
        this.evaluationService = evaluationService;
    }
}
