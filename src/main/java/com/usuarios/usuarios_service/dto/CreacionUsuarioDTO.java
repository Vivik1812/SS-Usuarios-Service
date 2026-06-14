package com.usuarios.usuarios_service.dto;

import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreacionUsuarioDTO {
    
    private String googleId;

    @NotBlank
    @Size(min = 8, max = 12)
    private String rut;

    @NotBlank
    @Size(max = 50)
    private String nombre;

    @Size(max = 50)
    private String apellido;

    @NotBlank
    @Email
    private String correo;

    @NotBlank
    @Pattern(regexp = "^[0-9+]{8,15}$", message = "Telefono invalido")
    private String telefono;

    private Set<Long> roles;
}
