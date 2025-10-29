package ytg.projetjavaytg.Controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ytg.projetjavaytg.Models.Apprenti;
import ytg.projetjavaytg.Services.AnneeAcademiqueService;
import ytg.projetjavaytg.Services.ApprentiService;
import ytg.projetjavaytg.Services.UtilisateurService;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class DashboardController {

    private final ApprentiService apprentiService;
    private final AnneeAcademiqueService anneeAcademiqueService;
    private final UtilisateurService utilisateurService;

    public DashboardController(ApprentiService apprentiService, AnneeAcademiqueService anneeAcademiqueService,
                               UtilisateurService utilisateurService) {
        this.apprentiService = apprentiService;
        this.anneeAcademiqueService = anneeAcademiqueService;
        this.utilisateurService = utilisateurService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        String prenom = utilisateurService.getPrenomByUsername(username);
        String anneeAcademique = anneeAcademiqueService.getAnneeAcademiqueEnCours();
        List<Apprenti> apprentis = apprentiService.getApprentisNonArchives().stream()
                .filter(a -> a.getAnneeAcademique() != null && a.getAnneeAcademique().equals(anneeAcademique))
                .collect(Collectors.toList());
        model.addAttribute("username", prenom);
        model.addAttribute("apprentis", apprentis);
        model.addAttribute("anneeAcademique", anneeAcademique);
        return "dashboard";
    }
}