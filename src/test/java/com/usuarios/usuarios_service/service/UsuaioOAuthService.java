package com.usuarios.usuarios_service.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.usuarios.usuarios_service.dto.CompletarPerfilDTO;
import com.usuarios.usuarios_service.model.Rol;
import com.usuarios.usuarios_service.model.Usuario;
import com.usuarios.usuarios_service.repository.RolRepository;
import com.usuarios.usuarios_service.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class UsuarioOAuthServiceTest {

    @Mock
    private UsuarioRepository usuRepo;

    @Mock
    private RolRepository rolRepo;

    @Mock
    private OAuth2User oauth2User;

    @InjectMocks
    private UsuarioOAuthService usuarioOAuthService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setGoogleId("google123");
        usuario.setCorreo("test@gmail.com");
        usuario.setNombre("Juan");
        usuario.setApellido("Perez");
        usuario.setPerfilCompleto(false);
    }

    @Test
    void procesarUsuarioGoogle_DeberiaRetornarUsuarioExistente() {

        when(oauth2User.getAttribute("sub"))
                .thenReturn("google123");

        when(usuRepo.findByGoogleId("google123"))
                .thenReturn(Optional.of(usuario));

        Usuario resultado =
                usuarioOAuthService.procesarUsuarioGoogle(oauth2User);

        assertEquals("google123", resultado.getGoogleId());

        verify(usuRepo, never()).save(any());
    }

    @Test
    void procesarUsuarioGoogle_DeberiaCrearUsuarioNuevo() {

        Rol rol = new Rol();
        rol.setNombre("ROLE_USER");

        when(oauth2User.getAttribute("sub"))
                .thenReturn("google123");

        when(oauth2User.getAttribute("email"))
                .thenReturn("nuevo@gmail.com");

        when(oauth2User.getAttribute("given_name"))
                .thenReturn("Pedro");

        when(oauth2User.getAttribute("family_name"))
                .thenReturn("Gomez");

        when(usuRepo.findByGoogleId("google123"))
                .thenReturn(Optional.empty());

        when(rolRepo.findByNombre("ROLE_USER"))
                .thenReturn(Optional.of(rol));

        when(usuRepo.save(any(Usuario.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        Usuario resultado =
                usuarioOAuthService.procesarUsuarioGoogle(oauth2User);

        assertEquals("Pedro", resultado.getNombre());
        assertEquals("Gomez", resultado.getApellido());
        assertFalse(resultado.getPerfilCompleto());
        assertEquals(1, resultado.getRoles().size());
    }

    @Test
    void procesarUsuarioGoogle_DeberiaAsignarSinApellido() {

        Rol rol = new Rol();
        rol.setNombre("ROLE_USER");

        when(oauth2User.getAttribute("sub"))
                .thenReturn("google123");

        when(oauth2User.getAttribute("email"))
                .thenReturn("nuevo@gmail.com");

        when(oauth2User.getAttribute("given_name"))
                .thenReturn("Pedro");

        when(oauth2User.getAttribute("family_name"))
                .thenReturn(null);

        when(usuRepo.findByGoogleId("google123"))
                .thenReturn(Optional.empty());

        when(rolRepo.findByNombre("ROLE_USER"))
                .thenReturn(Optional.of(rol));

        when(usuRepo.save(any(Usuario.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        Usuario resultado =
                usuarioOAuthService.procesarUsuarioGoogle(oauth2User);

        assertEquals("Sin apellido", resultado.getApellido());
    }

    @Test
    void completarPerfil_DeberiaCompletarPerfil() {

        CompletarPerfilDTO dto = new CompletarPerfilDTO();
        dto.setRut("11111111-1");
        dto.setTelefono("987654321");

        when(oauth2User.getAttribute("sub"))
                .thenReturn("google123");

        when(usuRepo.findByGoogleId("google123"))
                .thenReturn(Optional.of(usuario));

        when(usuRepo.existsByRut("11111111-1"))
                .thenReturn(false);

        when(usuRepo.save(any(Usuario.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        Usuario resultado =
                usuarioOAuthService.completarPerfil(oauth2User, dto);

        assertTrue(resultado.getPerfilCompleto());
        assertEquals("11111111-1", resultado.getRut());
    }

    @Test
    void completarPerfil_DeberiaFallarSiPerfilYaCompleto() {

        usuario.setPerfilCompleto(true);

        CompletarPerfilDTO dto = new CompletarPerfilDTO();

        when(oauth2User.getAttribute("sub"))
                .thenReturn("google123");

        when(usuRepo.findByGoogleId("google123"))
                .thenReturn(Optional.of(usuario));

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> usuarioOAuthService.completarPerfil(oauth2User, dto));

        assertEquals("El perfil ya esta completo", ex.getMessage());
    }

    @Test
    void completarPerfil_DeberiaFallarSiRutExiste() {

        CompletarPerfilDTO dto = new CompletarPerfilDTO();
        dto.setRut("11111111-1");

        when(oauth2User.getAttribute("sub"))
                .thenReturn("google123");

        when(usuRepo.findByGoogleId("google123"))
                .thenReturn(Optional.of(usuario));

        when(usuRepo.existsByRut("11111111-1"))
                .thenReturn(true);

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> usuarioOAuthService.completarPerfil(oauth2User, dto));

        assertEquals("El RUT ya esta registrado", ex.getMessage());
    }

    @Test
    void obtenerUsuarioActual_DeberiaRetornarUsuario() {

        when(oauth2User.getAttribute("sub"))
                .thenReturn("google123");

        when(usuRepo.findByGoogleId("google123"))
                .thenReturn(Optional.of(usuario));

        Usuario resultado =
                usuarioOAuthService.obtenerUsuarioActual(oauth2User);

        assertEquals("google123", resultado.getGoogleId());
    }
}