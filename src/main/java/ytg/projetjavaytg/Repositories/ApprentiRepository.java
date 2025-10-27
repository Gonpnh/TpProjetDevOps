package ytg.projetjavaytg.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ytg.projetjavaytg.Models.Apprenti;

import java.util.List;

public interface ApprentiRepository extends JpaRepository<Apprenti,Long> {

    // Récupère tous les apprentis dont l'entreprise a le nom exact (case-sensitive selon DB)
    @Query("select a from Apprenti a where a.entreprise.raisonSociale = :raisonSociale")
    List<Apprenti> findAllByRaisonSociale(@Param("raisonSociale") String raisonSociale);

    // Variante insensitive (fonctionne avec JPQL mais dépend du dialecte pour lower) :
    @Query("select a from Apprenti a where lower(a.entreprise.raisonSociale) = lower(:raisonSociale)")
    List<Apprenti> findAllByRaisonSocialeIgnoreCase(@Param("raisonSociale") String raisonSociale);
}
