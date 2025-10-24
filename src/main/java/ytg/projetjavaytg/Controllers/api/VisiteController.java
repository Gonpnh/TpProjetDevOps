package ytg.projetjavaytg.Controllers.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ytg.projetjavaytg.DTO.CreateVisiteDTO;
import ytg.projetjavaytg.Models.Visite;
import ytg.projetjavaytg.Services.VisiteService;
import ytg.projetjavaytg.Services.ApprentiService;

import java.time.LocalDate;
import java.util.List;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Visite")
@RestController
@RequestMapping("/api/visites")
public class VisiteController {

    private final VisiteService visiteService;
    private final ApprentiService apprentiService;

    public VisiteController(VisiteService visiteService, ApprentiService apprentiService) {
        this.visiteService = visiteService;
        this.apprentiService = apprentiService;
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

    @PostMapping("/simple")
    public ResponseEntity<?> createVisiteSimple(@RequestBody CreateVisiteDTO dto) {
        if (dto == null || dto.getApprentiId() == null || dto.getDateVisite() == null) {
            return ResponseEntity.badRequest().body("apprentiId and dateVisite required");
        }

        // parse date first (must be yyyy-MM-dd)
        final LocalDate parsedDate;
        try {
            parsedDate = LocalDate.parse(dto.getDateVisite());
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("dateVisite must be in format yyyy-MM-dd");
        }

        // Validate and normalize format (final so can be used in lambda)
        final String savedFormat;
        if (dto.getFormat() == null || dto.getFormat().trim().isEmpty()) {
            savedFormat = null;
        } else {
            String f = dto.getFormat().trim().toLowerCase();
            if (f.equals("présentiel") || f.equals("presentiel")) {
                savedFormat = "présentiel";
            } else if (f.equals("hybride")) {
                savedFormat = "hybride";
            } else if (f.equals("distance") || f.equals("à distance") || f.equals("a distance") || f.equals("adistance")) {
                savedFormat = "distance";
            } else {
                return ResponseEntity.badRequest().body("format must be one of: présentiel, hybride, distance");
            }
        }

        return apprentiService.getApprentiById(dto.getApprentiId())
                .map(apprenti -> {
                    try {
                        Visite v = new Visite();
                        v.setApprenti(apprenti);
                        v.setDateVisite(parsedDate);
                        v.setFormat(savedFormat);
                        v.setCommentaires(dto.getCommentaires());
                        Visite created = visiteService.createVisite(v);
                        return ResponseEntity.status(HttpStatus.CREATED).body(created);
                    } catch (Exception e) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
                    }
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Apprenti not found"));
    }

}
