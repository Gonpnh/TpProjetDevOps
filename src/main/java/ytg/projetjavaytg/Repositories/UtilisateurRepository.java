package ytg.projetjavaytg.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ytg.projetjavaytg.Models.Utilisateur;
import java.util.Optional;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    Optional<Utilisateur> findByUsername(String username);
    Optional<Utilisateur> findByEmail(String email);
}

