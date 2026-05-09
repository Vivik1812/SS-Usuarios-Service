package com.usuarios.usuarios_service.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.usuarios.usuarios_service.model.Usuario;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
    
    @Value("${jwt.secret}")
    private String SECRET_KEY;

    private SecretKey getKey(){
        return Keys.hmacShaKeyFor(
            SECRET_KEY.getBytes(StandardCharsets.UTF_8)
        );
    }

    public String generarToken(Usuario usuario){
        String roles = usuario.getRoles()
            .stream()
            .map(rol -> rol.getNombre())
            .collect(Collectors.joining(","));
        
            return Jwts.builder()
                .subject(usuario.getCorreo())
                .claim("id", usuario.getId())
                .claim("googleId", usuario.getGoogleId())
                .claim("roles", roles)
                .claim("perfilCompleto", usuario.getPerfilCompleto())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(getKey())
                .compact();
    }

    public String extraerCorreo(String token){
        return Jwts.parser()    
            .verifyWith(getKey())
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .getSubject();
    }

    public boolean tokenValido(String token){
        try{
            Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token);

            return true;
        } catch(Exception e){
            return false;
        }
    }
}
