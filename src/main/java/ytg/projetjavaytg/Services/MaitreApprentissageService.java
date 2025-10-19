package ytg.projetjavaytg.Services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ytg.projetjavaytg.Models.MaitreApprentissage;
import ytg.projetjavaytg.Repositories.MaitreApprentissageRepository;

import java.util.List;
import java.util.Optional;

@Service
public class MaitreApprentissageService {

    private final MaitreApprentissageRepository maitreApprentissageRepository;

    public MaitreApprentissageService(MaitreApprentissageRepository maitreApprentissageRepository) {
        this.maitreApprentissageRepository = maitreApprentissageRepository;
    }

    public List<MaitreApprentissage> getAllMaitresApprentissage() {
        return maitreApprentissageRepository.findAll();
    }

    public Optional<MaitreApprentissage> getMaitreApprentissageById(Long id) {
        return maitreApprentissageRepository.findById(id);
    }

    @Transactional
    public MaitreApprentissage createMaitreApprentissage(MaitreApprentissage maitreApprentissage) {
        return maitreApprentissageRepository.save(maitreApprentissage);
    }
}
