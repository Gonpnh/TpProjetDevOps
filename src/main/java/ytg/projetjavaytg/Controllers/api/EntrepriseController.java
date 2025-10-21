package ytg.projetjavaytg.Controllers.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ytg.projetjavaytg.Models.Entreprise;
import ytg.projetjavaytg.Services.EntrepriseService;

import java.util.List;

@Controller
public class EntrepriseController {

    private final EntrepriseService entrepriseService;

    public EntrepriseController(EntrepriseService entrepriseService) {
        this.entrepriseService = entrepriseService;
    }

    @GetMapping("/api/entreprises")
    @ResponseBody
    public ResponseEntity<List<Entreprise>> getAllEntreprisesApi() {
        List<Entreprise> entreprises = entrepriseService.getAllEntreprises();
        return ResponseEntity.ok(entreprises);
    }

    @GetMapping("/api/entreprises/{id}")
    @ResponseBody
    public ResponseEntity<Entreprise> getEntrepriseByIdApi(@PathVariable Long id) {
        return entrepriseService.getEntrepriseById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/api/entreprises")
    @ResponseBody
    public ResponseEntity<Entreprise> createEntrepriseApi(@RequestBody Entreprise entreprise) {
        Entreprise createdEntreprise = entrepriseService.createEntreprise(entreprise);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEntreprise);
    }

    @GetMapping("/entreprises")
    public String list(Model model) {
        model.addAttribute("entreprises", entrepriseService.getAllEntreprises());
        return "entreprise/list";
    }

    @GetMapping("/entreprises/create")
    public String createForm() {
        return "entreprise/create";
    }

    @PostMapping("/entreprises/create")
    public String createEntreprise(@ModelAttribute Entreprise entreprise, RedirectAttributes redirectAttributes) {
        try {
            entrepriseService.createEntreprise(entreprise);
            redirectAttributes.addFlashAttribute("success", "L'entreprise a été créée avec succès !");
            return "redirect:/entreprises";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la création : " + e.getMessage());
            return "redirect:/entreprises/create";
        }
    }

    @PostMapping("/entreprises/{id}/delete")
    public String deleteEntreprise(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            entrepriseService.getEntrepriseById(id).ifPresent(entreprise -> {
                entrepriseService.deleteEntreprise(id);
            });
            redirectAttributes.addFlashAttribute("success", "L'entreprise a été supprimée avec succès !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la suppression : " + e.getMessage());
        }
        return "redirect:/entreprises";
    }
}
