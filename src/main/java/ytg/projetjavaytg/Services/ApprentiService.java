package ytg.projetjavaytg.Services;

import org.springframework.stereotype.Service;
import ytg.projetjavaytg.Repositories.ApprentiRepository;
import ytg.projetjavaytg.Repositories.UtilisateurRepository;

@Service
public class ApprentiService {

    private final ApprentiRepository apprentiRepository;

    public ApprentiService(ApprentiRepository apprentiRepository) {
        this.apprentiRepository = apprentiRepository;
    }

    // mettre @Transactional sur les methodes qui modifient la base de données
}
