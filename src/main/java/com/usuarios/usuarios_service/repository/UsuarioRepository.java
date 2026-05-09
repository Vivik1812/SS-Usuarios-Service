package com.usuarios.usuarios_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.usuarios.usuarios_service.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByGoogleId(String googleId);
    Optional<Usuario> findByCorreo(String correo);
    boolean existsByRut(String rut);
}
