package ytg.projetjavaytg.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ytg.projetjavaytg.Models.Entreprise;
import ytg.projetjavaytg.Services.EntrepriseService;

@Controller
@RequestMapping("/entreprises")
public class EntrepriseViewController {

    private final EntrepriseService entrepriseService;

    public EntrepriseViewController(EntrepriseService entrepriseService) {
        this.entrepriseService = entrepriseService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("entreprises", entrepriseService.getAllEntreprises());
        return "entreprise/list";
    }

    @GetMapping("/create")
    public String createForm() {
        return "entreprise/create";
    }

    @PostMapping("/create")
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

    @PostMapping("/{id}/delete")
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

