package ytg.projetjavaytg.Services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ytg.projetjavaytg.Models.Apprenti;
import ytg.projetjavaytg.Repositories.ApprentiRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class ApprentiService {

    private final ApprentiRepository apprentiRepository;

    public ApprentiService(ApprentiRepository apprentiRepository) {
        this.apprentiRepository = apprentiRepository;
    }

    // mettre @Transactional sur les methodes qui modifient la base de données

    public List<Apprenti> getAllApprentis() {
        return apprentiRepository.findAll();
    }

    public Optional<Apprenti> getApprentiById(Long id) {
        return apprentiRepository.findById(id);
    }

    @Transactional
    public Apprenti createApprenti(Apprenti apprenti) {
        apprenti.setDateCreation(Instant.now());
        apprenti.setDateModification(Instant.now()); // Est-ce qu'on laisse la date de modification a null pour une creation ?
        if (apprenti.getArchive() == null) {
            apprenti.setArchive(false);
        }
        if (apprenti.getNiveau() == null) {
            apprenti.setNiveau("I1"); // valeur par defaut en base
        }
        return apprentiRepository.save(apprenti);
    }
}
