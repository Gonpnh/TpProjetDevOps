package ytg.projetjavaytg.Services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ytg.projetjavaytg.Models.Visite;
import ytg.projetjavaytg.Repositories.VisiteRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class VisiteService {

    private final VisiteRepository visiteRepository;

    public VisiteService(VisiteRepository visiteRepository) {
        this.visiteRepository = visiteRepository;
    }

    public List<Visite> getAllVisites() {
        return visiteRepository.findAll();
    }

    public Optional<Visite> getVisiteById(Long id) {
        return visiteRepository.findById(id);
    }

    @Transactional
    public Visite createVisite(Visite visite) {
        visite.setDateCreation(Instant.now());
        return visiteRepository.save(visite);
    }
}
