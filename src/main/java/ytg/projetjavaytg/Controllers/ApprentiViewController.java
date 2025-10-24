package ytg.projetjavaytg.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ytg.projetjavaytg.Models.Apprenti;
import ytg.projetjavaytg.Services.ApprentiService;
import ytg.projetjavaytg.Services.EntrepriseService;
import ytg.projetjavaytg.Services.MaitreApprentissageService;
import ytg.projetjavaytg.Services.UtilisateurService;

@Controller
@RequestMapping("/apprentis")
public class ApprentiViewController {

    private final ApprentiService apprentiService;
    private final EntrepriseService entrepriseService;
    private final MaitreApprentissageService maitreApprentissageService;
    private final UtilisateurService utilisateurService;

    public ApprentiViewController(ApprentiService apprentiService,
                                  EntrepriseService entrepriseService,
                                  MaitreApprentissageService maitreApprentissageService,
                                  UtilisateurService utilisateurService) {
        this.apprentiService = apprentiService;
        this.entrepriseService = entrepriseService;
        this.maitreApprentissageService = maitreApprentissageService;
        this.utilisateurService = utilisateurService;
    }

    @GetMapping("/create")
    public String createApprentiForm(Model model) {
        model.addAttribute("entreprises", entrepriseService.getAllEntreprises());
        model.addAttribute("maitres", maitreApprentissageService.getAllMaitresApprentissage());
        model.addAttribute("tuteurs", utilisateurService.getAllUtilisateurs());
        return "apprentice/createapprentice";
    }

    @PostMapping("/create")
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

    @GetMapping("/{id}")
    public String apprentiDetails(@PathVariable Long id, Model model) {
        return apprentiService.getApprentiById(id)
                .map(apprenti -> {
                    model.addAttribute("apprenti", apprenti);
                    return "apprentice/details";
                })
                .orElse("redirect:/dashboard");
    }

    @PostMapping("/{id}/delete")
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

