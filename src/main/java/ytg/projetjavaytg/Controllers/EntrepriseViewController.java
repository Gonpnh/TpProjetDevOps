package ytg.projetjavaytg.Controllers;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null ? authentication.getName() : "Utilisateur";
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("username", getCurrentUsername());
        model.addAttribute("entreprises", entrepriseService.getAllEntreprises());
        return "entreprise/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("username", getCurrentUsername());
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
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("error", "Impossible de supprimer cette entreprise. Elle est associée à un ou plusieurs apprentis. Veuillez d'abord modifier ou supprimer ces apprentis.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la suppression : " + e.getMessage());
        }
        return "redirect:/entreprises";
    }
}

