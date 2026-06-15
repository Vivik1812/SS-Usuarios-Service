package com.usuarios.usuarios_service.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.servlet.view.RedirectView;

import com.usuarios.usuarios_service.dto.CompletarPerfilDTO;
import com.usuarios.usuarios_service.dto.RespuestaUsuarioDTO;
import com.usuarios.usuarios_service.model.Usuario;
import com.usuarios.usuarios_service.security.JwtService;
import com.usuarios.usuarios_service.service.UsuarioOAuthService;
import com.usuarios.usuarios_service.service.UsuarioService;

class UsuarioControllerTest {

    @Mock
    private UsuarioService usuServ;

    @Mock
    private UsuarioOAuthService usuarioOAuthService;

    @Mock
    private JwtService jwtServ;

    @InjectMocks
    private UsuarioController controller;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        Field field = UsuarioController.class.getDeclaredField("frontUrl");
        field.setAccessible(true);
        field.set(controller, "http://localhost:5173");
    }

    @Test
    void getById_DeberiaRetornarUsuario() {

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Juan");

        when(usuServ.findById(1L))
                .thenReturn(usuario);

        RespuestaUsuarioDTO resultado = controller.getById(1L);

        assertEquals("Juan", resultado.getNombre());
    }

    @Test
    void oauthSuccess_DeberiaGenerarRedirectConToken() {

        OAuth2User oauthUser = mock(OAuth2User.class);

        Usuario usuario = new Usuario();
        usuario.setId(1L);

        when(usuarioOAuthService.procesarUsuarioGoogle(oauthUser))
                .thenReturn(usuario);

        when(jwtServ.generarToken(usuario))
                .thenReturn("jwt123");

        RedirectView resultado = controller.oauthSuccess(oauthUser);

        assertEquals(
                "http://localhost:5173/autenticacion?token=jwt123",
                resultado.getUrl());

        verify(jwtServ).generarToken(usuario);
    }

    @Test
    void obtenerUsuarioActual_DeberiaFallarSiAuthEsNull() {

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> controller.obtenerUsuarioActual(null));

        assertEquals(
                "Usuario no autenticado",
                ex.getMessage());
    }

    @Test
    void obtenerUsuarioActual_DeberiaRetornarUsuarioOAuth() {

        Authentication auth = mock(Authentication.class);
        OAuth2User oauthUser = mock(OAuth2User.class);

        Usuario usuario = new Usuario();
        usuario.setNombre("Juan");

        when(auth.isAuthenticated()).thenReturn(true);
        when(auth.getPrincipal()).thenReturn(oauthUser);

        when(usuarioOAuthService.obtenerUsuarioActual(oauthUser))
                .thenReturn(usuario);

        RespuestaUsuarioDTO resultado = controller.obtenerUsuarioActual(auth);

        assertEquals("Juan", resultado.getNombre());
    }

    @Test
    void obtenerUsuarioActual_DeberiaRetornarUsuarioDirecto() {

        Authentication auth = mock(Authentication.class);

        Usuario usuario = new Usuario();
        usuario.setNombre("Pedro");

        when(auth.isAuthenticated()).thenReturn(true);
        when(auth.getPrincipal()).thenReturn(usuario);

        RespuestaUsuarioDTO resultado = controller.obtenerUsuarioActual(auth);

        assertEquals("Pedro", resultado.getNombre());
    }

    @Test
    void obtenerUsuarioActual_DeberiaBuscarPorCorreo() {

        Authentication auth = mock(Authentication.class);

        Usuario usuario = new Usuario();
        usuario.setCorreo("test@gmail.com");

        when(auth.isAuthenticated()).thenReturn(true);
        when(auth.getPrincipal()).thenReturn("test@gmail.com");

        when(usuServ.findByCorreo("test@gmail.com"))
                .thenReturn(usuario);

        RespuestaUsuarioDTO resultado = controller.obtenerUsuarioActual(auth);

        assertEquals(
                "test@gmail.com",
                resultado.getCorreo());
    }

    @Test
    void obtenerUsuarioActual_DeberiaFallarConPrincipalDesconocido() {

        Authentication auth = mock(Authentication.class);

        when(auth.isAuthenticated()).thenReturn(true);
        when(auth.getPrincipal()).thenReturn(new Object());

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> controller.obtenerUsuarioActual(auth));

        assertEquals(
                "Usuario no autenticado",
                ex.getMessage());
    }

    @Test
    void completarPerfil_DeberiaCompletarPerfil() {

        OAuth2User oauthUser = mock(OAuth2User.class);

        CompletarPerfilDTO dto = new CompletarPerfilDTO();
        dto.setRut("11111111-1");
        dto.setTelefono("987654321");

        Usuario usuario = new Usuario();
        usuario.setRut("11111111-1");

        when(usuarioOAuthService.completarPerfil(
                oauthUser,
                dto))
                .thenReturn(usuario);

        RespuestaUsuarioDTO resultado = controller.completarPerfil(
                oauthUser,
                dto);

        assertEquals(
                "11111111-1",
                resultado.getRut());
    }

}
