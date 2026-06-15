package com.usuarios.usuarios_service.security;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.usuarios.usuarios_service.model.Rol;
import com.usuarios.usuarios_service.model.Usuario;

class JwtServiceTest {

    private JwtService jwtService;

    private Usuario usuario;

    @BeforeEach
    void setUp() throws Exception {

        jwtService = new JwtService();

        Field field = JwtService.class.getDeclaredField("SECRET_KEY");

        field.setAccessible(true);

        field.set(
                jwtService,
                "mi-clave-super-secreta-para-jwt-de-prueba-2026");

        Rol rol = new Rol();
        rol.setId(1L);
        rol.setNombre("ROLE_USER");

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setCorreo("test@gmail.com");
        usuario.setGoogleId("google123");
        usuario.setPerfilCompleto(true);
        usuario.setRoles(Set.of(rol));
    }

    @Test
    void generarToken_DeberiaRetornarToken() {

        String token = jwtService.generarToken(usuario);

        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    @Test
    void extraerCorreo_DeberiaRetornarCorreoCorrecto() {

        String token = jwtService.generarToken(usuario);

        String correo = jwtService.extraerCorreo(token);

        assertEquals(
                "test@gmail.com",
                correo);
    }

    @Test
    void tokenValido_DeberiaRetornarTrue() {

        String token = jwtService.generarToken(usuario);

        assertTrue(
                jwtService.tokenValido(token));
    }

    @Test
    void tokenValido_DeberiaRetornarFalse() {

        assertFalse(
                jwtService.tokenValido(
                        "token-invalido"));
    }

}