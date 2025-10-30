package ytg.projetjavaytg.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ytg.projetjavaytg.Models.Apprenti;

import java.util.List;

public interface ApprentiRepository extends JpaRepository<Apprenti,Long> {

    List<Apprenti> findByArchiveFalse();

    // Met à jour le niveau des apprentis avec JPQL
    @Modifying
    @Query("UPDATE Apprenti a SET a.niveau = :nouveauNiveau WHERE a.niveau = :ancienNiveau AND a.archive = false")
    int promouvoirApprentisByNiveau(@Param("ancienNiveau") String ancienNiveau, @Param("nouveauNiveau") String nouveauNiveau);

    @Modifying
    @Query("UPDATE Apprenti a SET a.archive = true WHERE a.niveau = 'I3' AND a.archive = false")
    int archiverApprentisI3();
}
