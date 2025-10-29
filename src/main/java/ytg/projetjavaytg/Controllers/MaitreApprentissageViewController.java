package ytg.projetjavaytg.Controllers;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ytg.projetjavaytg.Models.MaitreApprentissage;
import ytg.projetjavaytg.Services.EntrepriseService;
import ytg.projetjavaytg.Services.MaitreApprentissageService;
import ytg.projetjavaytg.Services.UtilisateurService;

@Controller
@RequestMapping("/maitres")
public class MaitreApprentissageViewController {

    private final MaitreApprentissageService maitreApprentissageService;
    private final EntrepriseService entrepriseService;
    private final UtilisateurService utilisateurService;

    public MaitreApprentissageViewController(MaitreApprentissageService maitreApprentissageService,
                                            EntrepriseService entrepriseService,
                                            UtilisateurService utilisateurService) {
        this.maitreApprentissageService = maitreApprentissageService;
        this.entrepriseService = entrepriseService;
        this.utilisateurService = utilisateurService;
    }

    private String getCurrentUserPrenom() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication != null ? authentication.getName() : "Utilisateur";
        return utilisateurService.getPrenomByUsername(username);
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("username", getCurrentUserPrenom());
        model.addAttribute("maitres", maitreApprentissageService.getAllMaitresApprentissage());
        return "maitreapprentissage/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("username", getCurrentUserPrenom());
        model.addAttribute("entreprises", entrepriseService.getAllEntreprises());
        return "maitreapprentissage/create";
    }

    @PostMapping("/create")
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

    @PostMapping("/{id}/delete")
    public String deleteMaitre(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            maitreApprentissageService.getMaitreApprentissageById(id).ifPresent(maitre -> {
                maitreApprentissageService.deleteMaitreApprentissage(id);
            });
            redirectAttributes.addFlashAttribute("success", "Le maître d'apprentissage a été supprimé avec succès !");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("error", "Impossible de supprimer ce maître d'apprentissage. Il est associé à un ou plusieurs apprentis. Veuillez d'abord modifier ou supprimer ces apprentis.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la suppression : " + e.getMessage());
        }
        return "redirect:/maitres";
    }
}

