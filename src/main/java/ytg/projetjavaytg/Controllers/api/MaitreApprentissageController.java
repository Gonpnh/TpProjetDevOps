package ytg.projetjavaytg.Controllers.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ytg.projetjavaytg.Repositories.MaitreApprentissageRepository;
import ytg.projetjavaytg.Services.MaitreApprentissageService;

@RestController
@RequestMapping("/api/maitreapprentissages")
public class MaitreApprentissageController {

    private final MaitreApprentissageService maitreApprentissageService;

    public MaitreApprentissageController(MaitreApprentissageService maitreApprentissageService) {
        this.maitreApprentissageService = maitreApprentissageService;
    }
}
