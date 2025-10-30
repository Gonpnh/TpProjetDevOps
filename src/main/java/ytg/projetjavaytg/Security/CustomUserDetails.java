package ytg.projetjavaytg.Security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ytg.projetjavaytg.Models.Utilisateur;
import java.util.Collection;
import java.util.Collections;

public record CustomUserDetails(Utilisateur utilisateur) implements UserDetails {

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(utilisateur.getRole()));
    }

    @Override
    public String getPassword() {
        return utilisateur.getPassword();
    }

    @Override
    public String getUsername() {
        return utilisateur.getUsername();
    }

    @Override
    public boolean isEnabled() {
        return utilisateur.getEnabled();
    }

}

