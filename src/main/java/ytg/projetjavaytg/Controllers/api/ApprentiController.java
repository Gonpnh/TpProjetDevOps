package ytg.projetjavaytg.Controllers.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ytg.projetjavaytg.Models.Apprenti;
import ytg.projetjavaytg.Services.ApprentiService;
import ytg.projetjavaytg.Services.EntrepriseService;
import ytg.projetjavaytg.Services.MaitreApprentissageService;
import ytg.projetjavaytg.Services.UtilisateurService;

import java.util.List;

@Controller
public class ApprentiController {

    private final ApprentiService apprentiService;
    private final EntrepriseService entrepriseService;
    private final MaitreApprentissageService maitreApprentissageService;
    private final UtilisateurService utilisateurService;

    public ApprentiController(ApprentiService apprentiService,
                             EntrepriseService entrepriseService,
                             MaitreApprentissageService maitreApprentissageService,
                             UtilisateurService utilisateurService) {
        this.apprentiService = apprentiService;
        this.entrepriseService = entrepriseService;
        this.maitreApprentissageService = maitreApprentissageService;
        this.utilisateurService = utilisateurService;
    }

    @GetMapping("/api/apprentis")
    @ResponseBody
    public ResponseEntity<List<Apprenti>> getAllApprentisApi() {
        List<Apprenti> apprentis = apprentiService.getAllApprentis();
        return ResponseEntity.ok(apprentis);
    }

    @GetMapping("/api/apprentis/{id}")
    @ResponseBody
    public ResponseEntity<Apprenti> getApprentiByIdApi(@PathVariable Long id) {
        return apprentiService.getApprentiById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/api/apprentis")
    @ResponseBody
    public ResponseEntity<Apprenti> createApprentiApi(@RequestBody Apprenti apprenti) {
        Apprenti createdApprenti = apprentiService.createApprenti(apprenti);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdApprenti);
    }

    @GetMapping("/apprentis/create")
    public String createApprentiForm(Model model) {
        model.addAttribute("entreprises", entrepriseService.getAllEntreprises());
        model.addAttribute("maitres", maitreApprentissageService.getAllMaitresApprentissage());
        model.addAttribute("tuteurs", utilisateurService.getAllUtilisateurs());
        return "apprentice/createapprentice";
    }

    @PostMapping("/apprentis/create")
    public String createApprenti(@ModelAttribute Apprenti apprenti,
                                @RequestParam Long entrepriseId,
                                @RequestParam Long maitreApprentissageId,
                                @RequestParam Long tuteurEnseignantId,
                                RedirectAttributes redirectAttributes) {
        try {
            entrepriseService.getEntrepriseById(entrepriseId).ifPresent(apprenti::setEntreprise);
            maitreApprentissageService.getMaitreApprentissageById(maitreApprentissageId)
                    .ifPresent(apprenti::setMaitreApprentissage);
            utilisateurService.getUtilisateurById(tuteurEnseignantId)
                    .ifPresent(apprenti::setTuteurEnseignant);
            apprentiService.createApprenti(apprenti);
            redirectAttributes.addFlashAttribute("success", "L'apprenti a été créé avec succès !");
            return "redirect:/dashboard";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la création de l'apprenti : " + e.getMessage());
            return "redirect:/apprentis/create";
        }
    }

    @GetMapping("/apprentis/{id}")
    public String apprentiDetails(@PathVariable Long id, Model model) {
        return apprentiService.getApprentiById(id)
                .map(apprenti -> {
                    model.addAttribute("apprenti", apprenti);
                    return "apprenti/details";
                })
                .orElse("redirect:/dashboard");
    }

    @PostMapping("/apprentis/{id}/delete")
    public String deleteApprenti(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            apprentiService.getApprentiById(id).ifPresent(apprenti -> {
                apprentiService.deleteApprenti(id);
            });
            redirectAttributes.addFlashAttribute("success", "L'apprenti a été supprimé avec succès !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la suppression : " + e.getMessage());
        }
        return "redirect:/dashboard";
    }
}
