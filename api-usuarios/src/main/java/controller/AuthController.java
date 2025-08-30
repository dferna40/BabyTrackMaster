package controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dto.LoginDTO;
import dto.GoogleTokenDTO;
import dto.RegistroUsuarioDTO;
import dto.TokenResponseDTO;
import entity.Usuario;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import service.AuthService;
import service.UsuarioService;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UsuarioService usuarioService;
    private final AuthService authService;

    public AuthController(UsuarioService usuarioService, AuthService authService) {
        this.usuarioService = usuarioService;
        this.authService = authService;
    }

    @Operation(summary = "Registro de usuario")
    @PostMapping("/register")
    public ResponseEntity<Usuario> registrar(@Valid @RequestBody RegistroUsuarioDTO dto) {
        Usuario creado = usuarioService.registrarNuevo(dto);
        return new ResponseEntity<Usuario>(creado, HttpStatus.CREATED);
    }

    @Operation(summary = "Login y emisión de JWT")
    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(@Valid @RequestBody LoginDTO dto) {
        String token = authService.login(dto);
        TokenResponseDTO body = new TokenResponseDTO(token, "Bearer");
        return ResponseEntity.ok().body(body);
    }

    @Operation(summary = "Login con Google y emisión de JWT")
    @PostMapping("/google")
    public ResponseEntity<TokenResponseDTO> loginGoogle(@Valid @RequestBody GoogleTokenDTO dto) {
        String token = authService.loginWithGoogle(dto.getToken());
        TokenResponseDTO body = new TokenResponseDTO(token, "Bearer");
        return ResponseEntity.ok().body(body);
    }
}
