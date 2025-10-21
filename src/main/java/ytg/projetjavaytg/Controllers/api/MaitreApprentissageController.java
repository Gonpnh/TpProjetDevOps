package ytg.projetjavaytg.Controllers.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ytg.projetjavaytg.Models.MaitreApprentissage;
import ytg.projetjavaytg.Services.EntrepriseService;
import ytg.projetjavaytg.Services.MaitreApprentissageService;

import java.util.List;

@Controller
public class MaitreApprentissageController {

    private final MaitreApprentissageService maitreApprentissageService;
    private final EntrepriseService entrepriseService;

    public MaitreApprentissageController(MaitreApprentissageService maitreApprentissageService,
                                        EntrepriseService entrepriseService) {
        this.maitreApprentissageService = maitreApprentissageService;
        this.entrepriseService = entrepriseService;
    }

    @GetMapping("/api/maitreapprentissages")
    @ResponseBody
    public ResponseEntity<List<MaitreApprentissage>> getAllMaitresApprentissageApi() {
        List<MaitreApprentissage> maitres = maitreApprentissageService.getAllMaitresApprentissage();
        return ResponseEntity.ok(maitres);
    }

    @GetMapping("/api/maitreapprentissages/{id}")
    @ResponseBody
    public ResponseEntity<MaitreApprentissage> getMaitreApprentissageByIdApi(@PathVariable Long id) {
        return maitreApprentissageService.getMaitreApprentissageById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/api/maitreapprentissages")
    @ResponseBody
    public ResponseEntity<MaitreApprentissage> createMaitreApprentissageApi(@RequestBody MaitreApprentissage maitreApprentissage) {
        MaitreApprentissage createdMaitre = maitreApprentissageService.createMaitreApprentissage(maitreApprentissage);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMaitre);
    }

    @GetMapping("/maitres")
    public String list(Model model) {
        model.addAttribute("maitres", maitreApprentissageService.getAllMaitresApprentissage());
        return "maitreapprentissage/list";
    }

    @GetMapping("/maitres/create")
    public String createForm(Model model) {
        model.addAttribute("entreprises", entrepriseService.getAllEntreprises());
        return "maitreapprentissage/create";
    }

    @PostMapping("/maitres/create")
    public String createMaitre(@ModelAttribute MaitreApprentissage maitre,
                              @RequestParam Long entrepriseId,
                              RedirectAttributes redirectAttributes) {
        try {
            entrepriseService.getEntrepriseById(entrepriseId).ifPresent(maitre::setEntreprise);
            maitreApprentissageService.createMaitreApprentissage(maitre);
            redirectAttributes.addFlashAttribute("success", "Le maître d'apprentissage a été créé avec succès !");
            return "redirect:/maitres";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la création : " + e.getMessage());
            return "redirect:/maitres/create";
        }
    }

    @PostMapping("/maitres/{id}/delete")
    public String deleteMaitre(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            maitreApprentissageService.getMaitreApprentissageById(id).ifPresent(maitre -> {
                maitreApprentissageService.deleteMaitreApprentissage(id);
            });
            redirectAttributes.addFlashAttribute("success", "Le maître d'apprentissage a été supprimé avec succès !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la suppression : " + e.getMessage());
        }
        return "redirect:/maitres";
    }
}
