package com.usuarios.usuarios_service.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import com.usuarios.usuarios_service.dto.CreacionUsuarioDTO;
import com.usuarios.usuarios_service.model.Usuario;
import com.usuarios.usuarios_service.repository.UsuarioRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Juan");
        usuario.setApellido("Pérez");
        usuario.setCorreo("juan@test.cl");
        usuario.setTelefono("123456789");
    }

    @Test
    void create_DeberiaGuardarUsuario() {
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        Usuario resultado = usuarioService.create(usuario);

        assertNotNull(resultado);
        assertEquals("Juan", resultado.getNombre());
        verify(usuarioRepository).save(usuario);
    }

    @Test
    void findAll_DeberiaRetornarListaUsuarios() {
        when(usuarioRepository.findAll())
                .thenReturn(List.of(usuario));

        List<Usuario> resultado = usuarioService.findAll();

        assertEquals(1, resultado.size());
        verify(usuarioRepository).findAll();
    }

    @Test
    void findById_DeberiaRetornarUsuario() {
        when(usuarioRepository.findById(1L))
                .thenReturn(Optional.of(usuario));

        Usuario resultado = usuarioService.findById(1L);

        assertEquals(1L, resultado.getId());
    }

    @Test
    void findById_DeberiaLanzarExcepcionSiNoExiste() {
        when(usuarioRepository.findById(1L))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> usuarioService.findById(1L));

        assertEquals("Usuario no encontrado", ex.getMessage());
    }

    @Test
    void findByCorreo_DeberiaRetornarUsuario() {
        when(usuarioRepository.findByCorreo("juan@test.cl"))
                .thenReturn(Optional.of(usuario));

        Usuario resultado =
                usuarioService.findByCorreo("juan@test.cl");

        assertEquals("juan@test.cl", resultado.getCorreo());
    }

    @Test
    void update_DeberiaActualizarCampos() {
        CreacionUsuarioDTO dto = new CreacionUsuarioDTO();
        dto.setNombre("Pedro");
        dto.setTelefono("987654321");

        when(usuarioRepository.findById(1L))
                .thenReturn(Optional.of(usuario));

        when(usuarioRepository.save(any(Usuario.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Usuario actualizado = usuarioService.update(1L, dto);

        assertEquals("Pedro", actualizado.getNombre());
        assertEquals("987654321", actualizado.getTelefono());
    }

    @Test
    void delete_DeberiaEliminarUsuario() {
        when(usuarioRepository.findById(1L))
                .thenReturn(Optional.of(usuario));

        usuarioService.delete(1L);

        verify(usuarioRepository).delete(usuario);
    }
}