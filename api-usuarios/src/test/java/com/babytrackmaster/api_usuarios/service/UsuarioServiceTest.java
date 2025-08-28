package com.babytrackmaster.api_usuarios.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import entity.Usuario;
import repository.UsuarioRepository;
import service.UsuarioService;

public class UsuarioServiceTest {

    private UsuarioRepository usuarioRepository;
    private UsuarioService usuarioService;

    @BeforeEach
    public void setUp() {
        usuarioRepository = mock(UsuarioRepository.class);
        usuarioService = new UsuarioService(usuarioRepository, null, null);
    }

    @Test
    public void eliminarLogicamente_desactivaUsuarioYRegistraFecha() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setHabilitado(true);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        usuarioService.eliminarLogicamente(1L);

        org.junit.jupiter.api.Assertions.assertFalse(usuario.isHabilitado());
        org.junit.jupiter.api.Assertions.assertNotNull(usuario.getFechaBaja());
        verify(usuarioRepository).save(usuario);
    }
}
