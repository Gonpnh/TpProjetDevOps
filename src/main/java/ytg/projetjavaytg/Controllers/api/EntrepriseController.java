package ytg.projetjavaytg.Controllers.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ytg.projetjavaytg.Repositories.EntrepriseRepository;
import ytg.projetjavaytg.Services.EntrepriseService;

@RestController
@RequestMapping("/api/entreprises")
public class EntrepriseController {

    private final EntrepriseService entrepriseService;

    public EntrepriseController(EntrepriseService entrepriseService) {
        this.entrepriseService = entrepriseService;
    }
}
