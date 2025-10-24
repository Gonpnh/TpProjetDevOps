package ytg.projetjavaytg.Services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ytg.projetjavaytg.Models.AnneeAcademique;
import ytg.projetjavaytg.Repositories.AnneeAcademiqueRepository;

import java.time.Instant;
import java.util.Optional;

@Service
public class AnneeAcademiqueService {

    private final AnneeAcademiqueRepository anneeAcademiqueRepository;

    public AnneeAcademiqueService(AnneeAcademiqueRepository anneeAcademiqueRepository) {
        this.anneeAcademiqueRepository = anneeAcademiqueRepository;
    }

    public String getAnneeAcademiqueEnCours() {
        Optional<AnneeAcademique> anneeActive = anneeAcademiqueRepository.findByActiveTrue();
        if (anneeActive.isPresent()) {
            return anneeActive.get().getAnnee();
        }
        int currentYear = java.time.Year.now().getValue();
        String anneeActuelle = currentYear + "-" + (currentYear + 1);
        creerEtActiverAnnee(anneeActuelle);
        return anneeActuelle;
    }

    @Transactional
    public void creerEtActiverAnnee(String nouvelleAnnee) {
        anneeAcademiqueRepository.findByActiveTrue().ifPresent(ancienneAnnee -> {
            ancienneAnnee.setActive(false);
            anneeAcademiqueRepository.save(ancienneAnnee);
        });
        AnneeAcademique annee = new AnneeAcademique();
        annee.setAnnee(nouvelleAnnee);
        annee.setActive(true);
        annee.setDateCreation(Instant.now());
        annee.setDateActivation(Instant.now());
        anneeAcademiqueRepository.save(annee);
    }

    public String calculerAnneeSuivante() {
        String anneeActuelle = getAnneeAcademiqueEnCours();
        String[] parties = anneeActuelle.split("-");
        if (parties.length == 2) {
            try {
                int anneeDebut = Integer.parseInt(parties[0]);
                int anneeFin = Integer.parseInt(parties[1]);
                return (anneeDebut + 1) + "-" + (anneeFin + 1);
            } catch (NumberFormatException e) {
                int currentYear = java.time.Year.now().getValue();
                return currentYear + "-" + (currentYear + 1);
            }
        }
        int currentYear = java.time.Year.now().getValue();
        return currentYear + "-" + (currentYear + 1);
    }
}


