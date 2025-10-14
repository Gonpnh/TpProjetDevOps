package ytg.projetjavaytg.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ytg.projetjavaytg.DTO.RegisterForm;
import ytg.projetjavaytg.Services.RegisterService;

@RestController
@RequestMapping("/register")
public class RegisterController {
    private final RegisterService registerService;

    public RegisterController(RegisterService registerService) {
        this.registerService = registerService;
    }

    @PostMapping
    public ResponseEntity<?> register(@RequestBody RegisterForm form) {
        try {
            registerService.register(form);
            return ResponseEntity.ok("Inscription réussie !");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

