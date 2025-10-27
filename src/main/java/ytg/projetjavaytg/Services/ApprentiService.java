package ytg.projetjavaytg.Services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ytg.projetjavaytg.Models.Apprenti;
import ytg.projetjavaytg.Repositories.ApprentiRepository;
import ytg.projetjavaytg.exception.ResourceNotFoundException;

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
        List<Apprenti> apprentis = apprentiRepository.findAll();
        if (apprentis.isEmpty()){
            throw new ResourceNotFoundException("Aucun apprenti existant en base");
        }
        return apprentis;
    }

    public Optional<Apprenti> getApprentiById(Long id) {
        Optional<Apprenti> apprenti = apprentiRepository.findById(id);
        if (apprenti.isPresent()){
            return apprenti;
        }
        throw new ResourceNotFoundException("Aucun apprenti trouvé avec l'id " + id);
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

    @Transactional
    public void deleteApprenti(Long id) {
        apprentiRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("L'apprenti que vous voulez supprimer n'existe pas" ));
        apprentiRepository.deleteById(id);
    }
}
