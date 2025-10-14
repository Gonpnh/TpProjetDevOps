package ytg.projetjavaytg.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class RegisterForm {
    private String username;
    private String password;
    private String prenom;
    private String nom;
    private String email;
    // private String role; // nullifiable
    // private Boolean enabled; // nullifiable
    // private Instant dateCreation; // nullifiable
    // private Instant dateDerniereConnexion; // nullifiable
}
