package service;


import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dto.RegistroUsuarioDTO;
import entity.Rol;
import entity.Usuario;
import repository.RolRepository;
import repository.UsuarioRepository;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, RolRepository rolRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Usuario registrarNuevo(RegistroUsuarioDTO dto) {
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado: " + dto.getEmail());
        }

        Rol rolUser = rolRepository.findByNombre("USER");
        if (rolUser == null) {
            rolUser = new Rol();
            rolUser.setNombre("USER");
            rolUser = rolRepository.save(rolUser);
        }

        Usuario u = new Usuario();
        u.setNombre(dto.getNombre());
        u.setApellidos(dto.getApellidos());
        u.setEmail(dto.getEmail());
        u.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        u.setRol(rolUser);
        u.setHabilitado(true);
        u.setFechaAlta(LocalDateTime.now());

        return usuarioRepository.save(u);
    }

    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }
}
