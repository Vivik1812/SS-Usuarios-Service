package com.usuarios.usuarios_service.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.usuarios.usuarios_service.model.Rol;
import com.usuarios.usuarios_service.repository.RolRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {
    
    private final RolRepository rolRepo;

    @Bean
    CommandLineRunner inicializarRoles(){

        return args -> {

            if(rolRepo.findByNombre("ROLE_USER").isEmpty()){
                
                Rol rol = new Rol();
                rol.setNombre("ROLE_USER");
                
                rolRepo.save(rol);
            }

            if(rolRepo.findByNombre("ROLE_ADMIN").isEmpty()){
                
                Rol rol = new Rol();
                rol.setNombre("ROLE_ADMIN");
                
                rolRepo.save(rol);
            }
        };
    }
}
