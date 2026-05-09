package com.usuarios.usuarios_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RespuestaLoginDTO {
    
    private String token;
    private RespuestaUsuarioDTO usuario;
}
