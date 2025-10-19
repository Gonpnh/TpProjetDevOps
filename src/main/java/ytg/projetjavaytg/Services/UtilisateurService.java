package ytg.projetjavaytg.Services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ytg.projetjavaytg.Models.Utilisateur;
import ytg.projetjavaytg.Repositories.UtilisateurRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;

    public UtilisateurService(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    // TODO il serait recommandé d'encoder le mot de passe avec BCrypt avant de le sauvegarder (en utilisant PasswordEncoder)
    public List<Utilisateur> getAllUtilisateurs() {
        return utilisateurRepository.findAll();
    }

    public Optional<Utilisateur> getUtilisateurById(Long id) {
        return utilisateurRepository.findById(id);
    }

    @Transactional
    public Utilisateur createUtilisateur(Utilisateur utilisateur) {
        utilisateur.setDateCreation(Instant.now());
        if (utilisateur.getEnabled() == null) {
            utilisateur.setEnabled(true);
        }
        if (utilisateur.getRole() == null) {
            utilisateur.setRole("ROLE_TUTEUR");
        }
        return utilisateurRepository.save(utilisateur);
    }
}
