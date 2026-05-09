package com.usuarios.usuarios_service.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.usuarios.usuarios_service.dto.CompletarPerfilDTO;
import com.usuarios.usuarios_service.model.Rol;
import com.usuarios.usuarios_service.model.Usuario;
import com.usuarios.usuarios_service.repository.RolRepository;
import com.usuarios.usuarios_service.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UsuarioOAuthService {
    
    private final UsuarioRepository usuRepo;
    private final RolRepository rolRepo;

    public Usuario procesarUsuarioGoogle(OAuth2User usuarioOAuth){
        String googleId = usuarioOAuth.getAttribute("sub");

        Optional<Usuario> usuarioExistente =
            usuRepo.findByGoogleId(googleId);

        if(usuarioExistente.isPresent()){
            return usuarioExistente.get();
        }

        Usuario usuario = new Usuario();

        usuario.setGoogleId(googleId);
        usuario.setCorreo(usuarioOAuth.getAttribute("email"));
        usuario.setNombre(usuarioOAuth.getAttribute("given_name"));
        usuario.setApellido(usuarioOAuth.getAttribute("family_name"));

        usuario.setPerfilCompleto(false);

        Rol rolUsuario = rolRepo
            .findByNombre("ROLE_USER")
            .orElseThrow();

        Set<Rol> roles = new HashSet<>();
        roles.add(rolUsuario);

        usuario.setRoles(roles);

        return usuRepo.save(usuario);
    }

    public Usuario completarPerfil(
        OAuth2User usuarioOAuth,
        CompletarPerfilDTO dto
    ){
        String googleId = usuarioOAuth.getAttribute("sub");


        Usuario usuario = usuRepo
            .findByGoogleId(googleId)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));;

        if(Boolean.TRUE.equals(usuario.getPerfilCompleto())){
            throw new RuntimeException("El perfil ya esta completo");
        }

        if(usuRepo.existsByRut(dto.getRut())){
            throw new RuntimeException("El RUT ya esta registrado");
        }

        usuario.setRut(dto.getRut());
        usuario.setTelefono(dto.getTelefono());
        usuario.setPerfilCompleto(true);

        return usuRepo.save(usuario);
    }

    public Usuario obtenerUsuarioActual(OAuth2User usuarioOAuth){
        String googleId = usuarioOAuth.getAttribute("sub");

        return usuRepo.findByGoogleId(googleId)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
}
