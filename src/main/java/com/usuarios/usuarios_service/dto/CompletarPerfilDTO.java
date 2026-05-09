package com.usuarios.usuarios_service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CompletarPerfilDTO {
    
    @NotBlank(message = "El RUT es obligatorio")
    private String rut;

    @NotBlank(message = "El telefono es obligatorio")
    private String telefono;
}
