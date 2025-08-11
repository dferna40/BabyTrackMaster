package controller;


import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import entity.Usuario;
import io.swagger.v3.oas.annotations.Operation;
import service.UsuarioService;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UserController {

    private final UsuarioService usuarioService;

    public UserController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Operation(summary = "Devuelve el usuario autenticado")
    @GetMapping("/me")
    public Usuario me(Authentication auth) {
        // auth.getName() = email (por UserDetailsService)
        return usuarioService.buscarPorEmail(auth.getName());
    }
}