package ytg.projetjavaytg.Controllers.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ytg.projetjavaytg.Models.Visite;
import ytg.projetjavaytg.Services.VisiteService;

import java.util.List;

@RestController
@RequestMapping("/api/visites")
public class VisiteController {

    private final VisiteService visiteService;

    public VisiteController(VisiteService visiteService) {
        this.visiteService = visiteService;
    }

    @GetMapping
    public ResponseEntity<List<Visite>> getAllVisites() {
        List<Visite> visites = visiteService.getAllVisites();
        return ResponseEntity.ok(visites);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Visite> getVisiteById(@PathVariable Long id) {
        return visiteService.getVisiteById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Visite> createVisite(@RequestBody Visite visite) {
        Visite createdVisite = visiteService.createVisite(visite);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdVisite);
    }
}
