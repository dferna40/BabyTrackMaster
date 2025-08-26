package repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import entity.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Usuario findByEmail(String email);
    boolean existsByEmail(String email);
    Usuario findFirstByEmail(String email);
    Usuario findFirstByNombre(String nombre);
    
 // Útil cuando no sabes si viene email o nombre:
    Usuario findFirstByEmailOrNombre(String email, String nombre);
}
