package com.babytrackmaster.api_usuarios.service;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import dto.LoginDTO;
import entity.Rol;
import entity.Usuario;
import repository.UsuarioRepository;
import repository.RolRepository;
import service.AuthService;
import service.JwtService;

public class AuthServiceTest {

        private UsuarioRepository usuarioRepository;
        private PasswordEncoder passwordEncoder;
        private JwtService jwtService;
        private RolRepository rolRepository;
        private AuthService authService;

        private Usuario usuario;

	@BeforeEach
	public void setUp() {
		usuarioRepository = Mockito.mock(UsuarioRepository.class);
		passwordEncoder = Mockito.mock(PasswordEncoder.class);
		jwtService = Mockito.mock(JwtService.class);

                rolRepository = Mockito.mock(RolRepository.class);
                authService = new AuthService(usuarioRepository, passwordEncoder, jwtService, rolRepository);

		usuario = new Usuario();
		usuario.setId(1L);
		usuario.setEmail("david@test.com");
		usuario.setPasswordHash("ENC");
		usuario.setHabilitado(true); // <--- IMPORTANTE
		Rol rol = new Rol();
		rol.setId(1L);
		rol.setNombre("ROLE_USER");
		usuario.setRol(rol);
	}

	@Test
        public void login_correcto_invocaJwtServiceYDevuelveToken() {
		LoginDTO dto = new LoginDTO();
		dto.setEmail("david@test.com");
		dto.setPassword("12345678");

		Mockito.when(usuarioRepository.findByEmail("david@test.com")).thenReturn(usuario);

		// Cubre ambos órdenes por si tu AuthService usa raw/encoded o encoded/raw
		Mockito.when(passwordEncoder.matches("12345678", "ENC")).thenReturn(true);
		Mockito.when(passwordEncoder.matches("ENC", "12345678")).thenReturn(true);

		Mockito.when(jwtService.generarToken("david@test.com")).thenReturn("HEADER.PAYLOAD.SIGN");

		String token = authService.login(dto);

		org.junit.jupiter.api.Assertions.assertNotNull(token);
		org.junit.jupiter.api.Assertions.assertEquals("HEADER.PAYLOAD.SIGN", token);

		Mockito.verify(usuarioRepository).findByEmail("david@test.com");
		Mockito.verify(passwordEncoder, Mockito.atLeastOnce()).matches(Mockito.anyString(), Mockito.anyString());
                Mockito.verify(jwtService).generarToken("david@test.com");
        }

        @Test
        public void login_usuarioDeshabilitado_lanzaExcepcion() {
                LoginDTO dto = new LoginDTO();
                dto.setEmail("david@test.com");
                dto.setPassword("12345678");

                usuario.setHabilitado(false);
                Mockito.when(usuarioRepository.findByEmail("david@test.com")).thenReturn(usuario);

                org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class,
                        () -> authService.login(dto));

                Mockito.verify(usuarioRepository).findByEmail("david@test.com");
        }
}