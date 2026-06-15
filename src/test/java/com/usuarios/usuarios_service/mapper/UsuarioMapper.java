package com.usuarios.usuarios_service.mapper;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.Test;

import com.usuarios.usuarios_service.dto.RespuestaUsuarioDTO;
import com.usuarios.usuarios_service.dto.CreacionUsuarioDTO;
import com.usuarios.usuarios_service.model.Rol;
import com.usuarios.usuarios_service.model.Usuario;

class UsuarioMapperTest {

    @Test
    void respuesta_DeberiaMapearUsuarioCompleto() {

        Rol rol = new Rol();
        rol.setId(1L);
        rol.setNombre("ROLE_USER");

        Usuario usuario = new Usuario();
        usuario.setId(10L);
        usuario.setGoogleId("google123");
        usuario.setRut("11111111-1");
        usuario.setNombre("Juan");
        usuario.setApellido("Perez");
        usuario.setCorreo("juan@test.cl");
        usuario.setTelefono("987654321");
        usuario.setPerfilCompleto(true);
        usuario.setRoles(Set.of(rol));

        RespuestaUsuarioDTO dto = UsuarioMapper.respuesta(usuario);

        assertEquals(10L, dto.getId());
        assertEquals("google123", dto.getGoogleId());
        assertEquals("11111111-1", dto.getRut());
        assertEquals("Juan", dto.getNombre());
        assertEquals("Perez", dto.getApellido());
        assertEquals("juan@test.cl", dto.getCorreo());
        assertEquals("987654321", dto.getTelefono());
        assertTrue(dto.getPerfilCompleto());

        assertNotNull(dto.getRoles());
        assertEquals(1, dto.getRoles().size());
    }

    @Test
    void respuesta_DeberiaAceptarRolesNull() {

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Juan");
        usuario.setRoles(null);

        RespuestaUsuarioDTO dto = UsuarioMapper.respuesta(usuario);

        assertNull(dto.getRoles());
    }

    @Test
    void creacion_DeberiaMapearDTOAUsuario() {

        CreacionUsuarioDTO dto = new CreacionUsuarioDTO();

        dto.setGoogleId("google123");
        dto.setRut("11111111-1");
        dto.setNombre("Juan");
        dto.setApellido("Perez");
        dto.setCorreo("juan@test.cl");
        dto.setTelefono("987654321");

        Usuario usuario = UsuarioMapper.creacion(dto);

        assertEquals("google123", usuario.getGoogleId());
        assertEquals("11111111-1", usuario.getRut());
        assertEquals("Juan", usuario.getNombre());
        assertEquals("Perez", usuario.getApellido());
        assertEquals("juan@test.cl", usuario.getCorreo());
        assertEquals("987654321", usuario.getTelefono());
    }

    

}
