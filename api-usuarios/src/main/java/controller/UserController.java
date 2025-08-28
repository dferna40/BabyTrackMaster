package controller;


import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        usuarioService.eliminarLogicamente(id);
        return ResponseEntity.noContent().build();
    }
}