package ytg.projetjavaytg.Services;

import org.springframework.stereotype.Service;
import ytg.projetjavaytg.Repositories.EntrepriseRepository;

@Service
public class EntrepriseService {

    private final EntrepriseRepository entrepriseRepository;

    public EntrepriseService(EntrepriseRepository entrepriseRepository) {
        this.entrepriseRepository = entrepriseRepository;
    }
}
