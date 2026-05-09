package com.usuarios.usuarios_service.dto;

import java.time.LocalDateTime;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RespuestaUsuarioDTO {

    private Long id;
    private String googleId;
    private String rut;
    private String nombre;
    private String apellido;
    private String correo;
    private String telefono;
    
    private LocalDateTime creacion;
    private LocalDateTime actualizacion;

    private Set<RolDTO> roles;

    private Boolean perfilCompleto;
}
