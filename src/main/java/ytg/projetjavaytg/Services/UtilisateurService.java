package ytg.projetjavaytg.Services;

import org.springframework.stereotype.Service;
import ytg.projetjavaytg.Repositories.UtilisateurRepository;

@Service
public class UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;

    public UtilisateurService(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }
}
