package com.usuarios.usuarios_service.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.beans.factory.annotation.Value;

import com.usuarios.usuarios_service.dto.CompletarPerfilDTO;
import com.usuarios.usuarios_service.dto.RespuestaUsuarioDTO;
import com.usuarios.usuarios_service.mapper.UsuarioMapper;
import com.usuarios.usuarios_service.model.Usuario;
import com.usuarios.usuarios_service.security.JwtService;
import com.usuarios.usuarios_service.service.UsuarioOAuthService;
import com.usuarios.usuarios_service.service.UsuarioService;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    @Value("${app.frontUrl}")
    private String frontUrl;
    private final UsuarioService usuServ;
    private final UsuarioOAuthService usuarioOAuthService;
    private final JwtService jwtServ;

    @GetMapping()
    public List<RespuestaUsuarioDTO> getAll() {
        return usuServ.findAll()
                .stream()
                .map(UsuarioMapper::respuesta)
                .toList();
    }

    @GetMapping("/oauth-success")
    public RedirectView oauthSuccess(
            @AuthenticationPrincipal OAuth2User usuarioOAuth) {
        Usuario usuario = usuarioOAuthService.procesarUsuarioGoogle(usuarioOAuth);

        String token = jwtServ.generarToken(usuario);

        return new RedirectView(
                frontUrl + "/autenticacion?token=" + token);
    }

    @GetMapping("/me")
    public RespuestaUsuarioDTO obtenerUsuarioActual(
            Authentication auth) {

        if(auth == null || !auth.isAuthenticated()){
            throw new RuntimeException("Usuario no autenticado");
        }

        Object principal = auth.getPrincipal();

        if (principal instanceof OAuth2User usuarioOAuth) {
            Usuario usuario = usuarioOAuthService.obtenerUsuarioActual(usuarioOAuth);
            return UsuarioMapper.respuesta(usuario);
        }

        if(principal instanceof Usuario usuario){
            return UsuarioMapper.respuesta(usuario);
        }

        if(principal instanceof String correo){
                Usuario usuario = usuServ.findByCorreo(correo);
            return UsuarioMapper.respuesta(usuario);
        }

        throw new RuntimeException("Usuario no autenticado");
    }

    @PutMapping("/completar/perfil")
    public RespuestaUsuarioDTO completarPerfil(
            @AuthenticationPrincipal OAuth2User usuarioOAuth,
            @Valid @RequestBody CompletarPerfilDTO dto) {
        Usuario usuario = usuarioOAuthService.completarPerfil(usuarioOAuth, dto);
        return UsuarioMapper.respuesta(usuario);
    }

    @GetMapping("/{id}")
    public RespuestaUsuarioDTO getById(@PathVariable Long id) {
        Usuario usuario = usuServ.findById(id);
        return UsuarioMapper.respuesta(usuario);
    }
}
