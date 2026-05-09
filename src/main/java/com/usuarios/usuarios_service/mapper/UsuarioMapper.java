package com.usuarios.usuarios_service.mapper;

import java.util.stream.Collectors;

import com.usuarios.usuarios_service.dto.CreacionUsuarioDTO;
import com.usuarios.usuarios_service.dto.RespuestaUsuarioDTO;
import com.usuarios.usuarios_service.dto.RolDTO;
import com.usuarios.usuarios_service.model.Usuario;

public class UsuarioMapper {
    
    public static RespuestaUsuarioDTO respuesta(Usuario usuario){
        RespuestaUsuarioDTO dto = new RespuestaUsuarioDTO();

        dto.setId(usuario.getId());
        dto.setGoogleId(usuario.getGoogleId());
        dto.setRut(usuario.getRut());
        dto.setNombre(usuario.getNombre());
        dto.setApellido(usuario.getApellido());
        dto.setCorreo(usuario.getCorreo());
        dto.setTelefono(usuario.getTelefono());
        dto.setCreacion(usuario.getCreacion());
        dto.setActualizacion(usuario.getActualizacion());
        dto.setPerfilCompleto(usuario.getPerfilCompleto());

        dto.setRoles(
            usuario.getRoles() == null ? null :
            usuario.getRoles().stream().map(rol -> {
                RolDTO r = new RolDTO();
                r.setId(rol.getId());
                r.setNombre(rol.getNombre());
                return r;
            }).collect(Collectors.toSet())
        );

        return dto;
    }

    public static Usuario creacion(CreacionUsuarioDTO dto){
        Usuario usuario = new Usuario();

        usuario.setGoogleId(dto.getGoogleId());
        usuario.setRut(dto.getRut());
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setCorreo(dto.getCorreo());
        usuario.setTelefono(dto.getTelefono());

        return usuario;
        
    }
}
