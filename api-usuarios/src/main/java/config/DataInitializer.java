package config;

import java.time.LocalDateTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import entity.Rol;
import entity.Usuario;
import repository.RolRepository;
import repository.UsuarioRepository;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner seedData(final RolRepository rolRepo,
                                      final UsuarioRepository usuarioRepo) {
        return new CommandLineRunner() {
            public void run(String... args) throws Exception {
                // ROLE_ADMIN
                Rol adminRol = rolRepo.findByNombre("ROLE_ADMIN");
                if (adminRol == null) {
                    adminRol = new Rol();
                    adminRol.setNombre("ROLE_ADMIN");
                    adminRol = rolRepo.save(adminRol);
                }

                // Admin user
                String adminEmail = "admin@babytrackmaster.local";
                Usuario admin = usuarioRepo.findByEmail(adminEmail);
                if (admin == null) {
                    admin = new Usuario();
                    admin.setNombre("Administrador");
                    admin.setApellidos("Sistema");
                    admin.setEmail(adminEmail);
                    admin.setHabilitado(true);
                    admin.setFechaAlta(LocalDateTime.now());
                    admin.setRol(adminRol);

                    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                    admin.setPasswordHash(encoder.encode("Admin123!")); // cambia en prod
                    usuarioRepo.save(admin);
                }
            }
        };
    }
}
