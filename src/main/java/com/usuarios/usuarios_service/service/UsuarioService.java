package com.usuarios.usuarios_service.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.usuarios.usuarios_service.dto.CreacionUsuarioDTO;
import com.usuarios.usuarios_service.model.Usuario;
import com.usuarios.usuarios_service.repository.UsuarioRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuRepo;

    public Usuario create(Usuario usuario) {
        return usuRepo.save(usuario);
    }

    public List<Usuario> findAll() {
        return usuRepo.findAll();
    }

    public Usuario findByCorreo(String correo){
        return usuRepo.findByCorreo(correo)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    public Usuario findById(Long id) {
        return usuRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    public Usuario update(Long id, CreacionUsuarioDTO nuevoUsuario) {
        Usuario usuario = findById(id);


        if (nuevoUsuario.getRut() != null) {
            usuario.setRut(nuevoUsuario.getRut());

        }
        if (nuevoUsuario.getNombre() != null) {
            usuario.setNombre(nuevoUsuario.getNombre());
        }
        if (nuevoUsuario.getApellido() != null) {
            usuario.setApellido(nuevoUsuario.getApellido());
        }
        if (nuevoUsuario.getCorreo() != null) {
            usuario.setCorreo(nuevoUsuario.getCorreo());
        }
        if (nuevoUsuario.getTelefono() != null) {
            usuario.setTelefono(nuevoUsuario.getTelefono());
        }

        return usuRepo.save(usuario);
    }

    public void delete(Long id) {
        Usuario usuario = findById(id);
        usuRepo.delete(usuario);
    }
}
