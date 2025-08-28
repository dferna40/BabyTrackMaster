package security;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import entity.Usuario;
import repository.UsuarioRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
    private UsuarioRepository usuarioRepository;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario u = usuarioRepository.findByEmailAndHabilitadoTrue(username);
        if (u == null) {
            throw new UsernameNotFoundException("Usuario no encontrado: " + username);
        }
        List<GrantedAuthority> auths = new ArrayList<GrantedAuthority>();
        if (u.getRol() != null && u.getRol().getNombre() != null) {
            auths.add(new SimpleGrantedAuthority("ROLE_" + u.getRol().getNombre()));
        }
        return new User(u.getEmail(), u.getPasswordHash(), u.isHabilitado(),
                true, true, true, auths);
    }
}