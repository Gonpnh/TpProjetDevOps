package ytg.projetjavaytg.Services;

import org.springframework.stereotype.Service;
import ytg.projetjavaytg.Repositories.VisiteRepository;

@Service
public class VisiteService {

    private final VisiteRepository visiteRepository;

    public VisiteService(VisiteRepository visiteRepository) {
        this.visiteRepository = visiteRepository;
    }
}
