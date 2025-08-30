package service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.types.User;
import com.restfb.types.DebugTokenInfo;

import dto.LoginDTO;
import entity.Rol;
import entity.Usuario;
import repository.RolRepository;
import repository.UsuarioRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Value("${google.client-id:}")
    private String googleClientId;

    @Value("${facebook.client-id:}")
    private String facebookClientId;

    @Value("${facebook.client-secret:}")
    private String facebookClientSecret;

    public AuthService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, JwtService jwtService, RolRepository rolRepository) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.rolRepository = rolRepository;
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

    public String loginWithGoogle(String idTokenString) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance())
                    .setAudience(Collections.singletonList(googleClientId))
                    .build();
            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken == null) {
                throw new IllegalArgumentException("Token de Google inválido");
            }
            String email = idToken.getPayload().getEmail();
            Usuario u = usuarioRepository.findByEmail(email);
            if (u == null) {
                Rol rolUser = rolRepository.findByNombre("USER");
                if (rolUser == null) {
                    rolUser = new Rol();
                    rolUser.setNombre("USER");
                    rolUser = rolRepository.save(rolUser);
                }
                u = new Usuario();
                u.setNombre((String) idToken.getPayload().get("given_name"));
                u.setApellidos((String) idToken.getPayload().get("family_name"));
                u.setEmail(email);
                u.setPasswordHash(passwordEncoder.encode(UUID.randomUUID().toString()));
                u.setRol(rolUser);
                u.setHabilitado(true);
                u.setFechaAlta(LocalDateTime.now());
                usuarioRepository.save(u);
            }
            return jwtService.generarToken(u.getEmail());
        } catch (Exception e) {
            throw new IllegalArgumentException("Token de Google inválido", e);
        }
    }

    public String loginWithFacebook(String accessToken) {
        try {
            FacebookClient userClient = new DefaultFacebookClient(accessToken, Version.LATEST);
            FacebookClient appClient = new DefaultFacebookClient(facebookClientId + "|" + facebookClientSecret, Version.LATEST);
            DebugTokenInfo info = appClient.debugToken(accessToken);
            if (info == null || !info.isValid() || !facebookClientId.equals(info.getAppId())) {
                throw new IllegalArgumentException("Token de Facebook inválido");
            }
            User fbUser = userClient.fetchObject("me", User.class,
                    Parameter.with("fields", "email,first_name,last_name"));
            String email = fbUser.getEmail();
            Usuario u = usuarioRepository.findByEmail(email);
            if (u == null) {
                Rol rolUser = rolRepository.findByNombre("USER");
                if (rolUser == null) {
                    rolUser = new Rol();
                    rolUser.setNombre("USER");
                    rolUser = rolRepository.save(rolUser);
                }
                u = new Usuario();
                u.setNombre(fbUser.getFirstName());
                u.setApellidos(fbUser.getLastName());
                u.setEmail(email);
                u.setPasswordHash(passwordEncoder.encode(UUID.randomUUID().toString()));
                u.setRol(rolUser);
                u.setHabilitado(true);
                u.setFechaAlta(LocalDateTime.now());
                usuarioRepository.save(u);
            }
            return jwtService.generarToken(u.getEmail());
        } catch (Exception e) {
            throw new IllegalArgumentException("Token de Facebook inválido", e);
        }
    }
}