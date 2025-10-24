package ytg.projetjavaytg.Controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ytg.projetjavaytg.Models.Apprenti;
import ytg.projetjavaytg.Services.ApprentiService;

import java.util.List;

@Controller
@RequestMapping("/recherche")
public class RechercheViewController {

    private final ApprentiService apprentiService;

    public RechercheViewController(ApprentiService apprentiService) {
        this.apprentiService = apprentiService;
    }

    @GetMapping
    public String afficherPageRecherche(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        List<Apprenti> tousLesApprentis = apprentiService.getAllApprentis();
        model.addAttribute("username", username);
        model.addAttribute("apprentis", tousLesApprentis);
        model.addAttribute("totalApprentis", tousLesApprentis.size());
        return "recherche/liste";
    }
}

