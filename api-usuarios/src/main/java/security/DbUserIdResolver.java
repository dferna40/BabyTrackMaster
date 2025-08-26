package security;

import org.springframework.stereotype.Component;

import config.UserIdResolver;
import entity.Usuario;
import repository.UsuarioRepository;

@Component
public class DbUserIdResolver implements UserIdResolver {

    private final UsuarioRepository repo;

    public DbUserIdResolver(UsuarioRepository repo) {
        this.repo = repo;
    }

    public Long resolve(String username) {
        if (username == null) {
            return null;
        }
        String u = username.trim();
        Usuario usr = null;

        // Si parece email, intentamos por email primero
        if (u.indexOf('@') > 0) {
            usr = repo.findFirstByEmail(u);
        }

        // Si no es email o no lo encontró, probamos por nombre (login)
        if (usr == null) {
            usr = repo.findFirstByNombre(u);
        }

        if (usr != null) {
            return usr.getId();
        }
        return null;
    }
}
