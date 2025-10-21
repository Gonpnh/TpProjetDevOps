package ytg.projetjavaytg.Controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ytg.projetjavaytg.Models.Apprenti;
import ytg.projetjavaytg.Services.ApprentiService;

import java.time.Year;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class DashboardController {

    private final ApprentiService apprentiService;

    public DashboardController(ApprentiService apprentiService) {
        this.apprentiService = apprentiService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        int currentYear = Year.now().getValue();
        String anneeAcademique = currentYear + "-" + (currentYear + 1);
        List<Apprenti> apprentis = apprentiService.getAllApprentis().stream()
                .filter(a -> a.getAnneeAcademique().equals(anneeAcademique) && !a.getArchive())
                .collect(Collectors.toList());
        model.addAttribute("username", username);
        model.addAttribute("apprentis", apprentis);
        model.addAttribute("anneeAcademique", anneeAcademique);
        return "dashboard";
    }
}