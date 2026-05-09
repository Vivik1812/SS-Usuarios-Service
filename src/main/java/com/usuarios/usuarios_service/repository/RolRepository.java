package com.usuarios.usuarios_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.usuarios.usuarios_service.model.Rol;

public interface RolRepository extends JpaRepository<Rol, Long>{

    Optional<Rol> findByNombre(String nombre);

}
