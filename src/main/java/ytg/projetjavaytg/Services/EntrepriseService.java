package ytg.projetjavaytg.Services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ytg.projetjavaytg.Models.Entreprise;
import ytg.projetjavaytg.Repositories.EntrepriseRepository;

import java.util.List;
import java.util.Optional;

@Service
public class EntrepriseService {

    private final EntrepriseRepository entrepriseRepository;

    public EntrepriseService(EntrepriseRepository entrepriseRepository) {
        this.entrepriseRepository = entrepriseRepository;
    }

    public List<Entreprise> getAllEntreprises() {
        return entrepriseRepository.findAll();
    }

    public Optional<Entreprise> getEntrepriseById(Long id) {
        return entrepriseRepository.findById(id);
    }

    @Transactional
    public Entreprise createEntreprise(Entreprise entreprise) {
        return entrepriseRepository.save(entreprise);
    }

    @Transactional
    public void deleteEntreprise(Long id) {
        entrepriseRepository.deleteById(id);
    }
}
