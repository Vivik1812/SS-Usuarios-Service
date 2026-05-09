package com.usuarios.usuarios_service.model;

import java.time.LocalDateTime;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usuarios",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = "rut"),
        @UniqueConstraint(columnNames = "correo"),
        @UniqueConstraint(columnNames = "google_id"),
    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "google_id", unique = true)
    private String googleId;


    @Size(min = 8, max = 12)
    @Column(unique = true)
    private String rut;

    @NotBlank
    @Size(max = 50)
    @Column(nullable = false)
    private String nombre;

    @NotBlank
    @Size(max = 50)
    @Column(nullable = false)
    private String apellido;

    @NotBlank
    @Email
    @Column(nullable = false, unique = true)
    private String correo;

    @Pattern(regexp = "^[0-9+]{8,15}$", message = "Telefono invalido")
    private String telefono;

    @Column(nullable = false, updatable = false)
    private LocalDateTime creacion;

    @Column(nullable = false)
    private LocalDateTime actualizacion;

    @Column(nullable = false)
    private Boolean perfilCompleto = false;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "usuario_roles",
        joinColumns = @JoinColumn(name = "usuario_id"),
        inverseJoinColumns = @JoinColumn(name = "rol_id")
    )
    private Set<Rol> roles;
    
    @PrePersist
    protected void Creacion(){
        this.creacion = LocalDateTime.now();
        this.actualizacion = LocalDateTime.now();
    }

    @PreUpdate
    protected void Actualizacion(){
        this.actualizacion = LocalDateTime.now();
    }
}
