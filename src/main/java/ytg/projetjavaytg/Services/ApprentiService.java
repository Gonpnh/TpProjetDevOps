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
    private final AnneeAcademiqueService anneeAcademiqueService;

    public ApprentiService(ApprentiRepository apprentiRepository,
                          AnneeAcademiqueService anneeAcademiqueService) {
        this.apprentiRepository = apprentiRepository;
        this.anneeAcademiqueService = anneeAcademiqueService;
    }

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
        if (apprenti.getAnneeAcademique() == null || apprenti.getAnneeAcademique().isEmpty()) {
            String anneeEnCours = anneeAcademiqueService.getAnneeAcademiqueEnCours();
            apprenti.setAnneeAcademique(anneeEnCours);
        }
        return apprentiRepository.save(apprenti);
    }

    @Transactional
    public void deleteApprenti(Long id) {
        apprentiRepository.deleteById(id);
    }

    public List<Apprenti> getApprentisNonArchives() {
        return apprentiRepository.findByArchiveFalse();
    }

    @Transactional
    public void creerNouvelleAnneeAcademique(String nouvelleAnnee) {
        anneeAcademiqueService.creerEtActiverAnnee(nouvelleAnnee);
        apprentiRepository.archiverApprentisI3();
        apprentiRepository.promouvoirApprentisByNiveau("I2", "I3");
        apprentiRepository.promouvoirApprentisByNiveau("I1", "I2");
        List<Apprenti> apprentisActifs = apprentiRepository.findByArchiveFalse();
        for (Apprenti apprenti : apprentisActifs) {
            apprenti.setAnneeAcademique(nouvelleAnnee);
            apprenti.setDateModification(Instant.now());
            apprentiRepository.save(apprenti);
        }
    }

    @Transactional
    public Apprenti updateApprenti(Long id, Apprenti apprentiDetails) {
        Optional<Apprenti> apprentiOpt = apprentiRepository.findById(id);
        if (apprentiOpt.isPresent()) {
            Apprenti apprenti = apprentiOpt.get();
            apprenti.setNom(apprentiDetails.getNom());
            apprenti.setPrenom(apprentiDetails.getPrenom());
            apprenti.setEmail(apprentiDetails.getEmail());
            apprenti.setTelephone(apprentiDetails.getTelephone());
            apprenti.setProgramme(apprentiDetails.getProgramme());
            apprenti.setAnneeAcademique(apprentiDetails.getAnneeAcademique());
            apprenti.setMajeure(apprentiDetails.getMajeure());
            apprenti.setNiveau(apprentiDetails.getNiveau());
            apprenti.setEntreprise(apprentiDetails.getEntreprise());
            apprenti.setMaitreApprentissage(apprentiDetails.getMaitreApprentissage());
            apprenti.setMissionMotsCles(apprentiDetails.getMissionMotsCles());
            apprenti.setMissionMetierCible(apprentiDetails.getMissionMetierCible());
            apprenti.setMissionCommentaires(apprentiDetails.getMissionCommentaires());
            apprenti.setFeedbackTuteur(apprentiDetails.getFeedbackTuteur());
            apprenti.setRemarquesGenerales(apprentiDetails.getRemarquesGenerales());
            apprenti.setDateModification(Instant.now());
            return apprentiRepository.save(apprenti);
        }
        return null;
    }
}
