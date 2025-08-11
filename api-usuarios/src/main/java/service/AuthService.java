package service;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import dto.LoginDTO;
import entity.Usuario;
import repository.UsuarioRepository;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public String login(LoginDTO dto) {
        Usuario u = usuarioRepository.findByEmail(dto.getEmail());
        if (u == null || !u.isHabilitado()) {
            throw new IllegalArgumentException("Credenciales inválidas");
        }
        if (!passwordEncoder.matches(dto.getPassword(), u.getPasswordHash())) {
            throw new IllegalArgumentException("Credenciales inválidas");
        }
        return jwtService.generarToken(u.getEmail());
    }
}