package ytg.projetjavaytg.Controllers.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ytg.projetjavaytg.Repositories.ApprentiRepository;
import ytg.projetjavaytg.Services.ApprentiService;

@RestController
@RequestMapping("/api/apprentis")
public class ApprentiController {

    private final ApprentiService apprentiService;

    public ApprentiController(ApprentiService apprentiService) {
        this.apprentiService = apprentiService;
    }

}
