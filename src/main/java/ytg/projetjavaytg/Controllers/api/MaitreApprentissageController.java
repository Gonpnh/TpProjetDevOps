package ytg.projetjavaytg.Controllers.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ytg.projetjavaytg.Models.MaitreApprentissage;
import ytg.projetjavaytg.Services.MaitreApprentissageService;

import java.util.List;

@RestController
@RequestMapping("/api/maitreapprentissages")
public class MaitreApprentissageController {

    private final MaitreApprentissageService maitreApprentissageService;

    public MaitreApprentissageController(MaitreApprentissageService maitreApprentissageService) {
        this.maitreApprentissageService = maitreApprentissageService;
    }

    @GetMapping
    public ResponseEntity<List<MaitreApprentissage>> getAllMaitresApprentissage() {
        List<MaitreApprentissage> maitres = maitreApprentissageService.getAllMaitresApprentissage();
        return ResponseEntity.ok(maitres);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaitreApprentissage> getMaitreApprentissageById(@PathVariable Long id) {
        return maitreApprentissageService.getMaitreApprentissageById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<MaitreApprentissage> createMaitreApprentissage(@RequestBody MaitreApprentissage maitreApprentissage) {
        MaitreApprentissage createdMaitre = maitreApprentissageService.createMaitreApprentissage(maitreApprentissage);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMaitre);
    }
}
