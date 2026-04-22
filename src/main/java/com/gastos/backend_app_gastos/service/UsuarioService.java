package com.gastos.backend_app_gastos.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.gastos.backend_app_gastos.model.Usuario;
import com.gastos.backend_app_gastos.repository.UsuarioRepository;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Usuario findById(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
    }

    @Transactional(readOnly = true)
    public Usuario findByUsername(String username) {
        return usuarioRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con email: " + username));
    }

    @Transactional
    public Usuario save(Usuario usuario) {
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public Usuario update(Long id, Usuario usuarioDetails) {
        Usuario usuarioExistente = findById(id);

        if (usuarioDetails.getNombre() != null && !usuarioDetails.getNombre().isEmpty()) {
            usuarioExistente.setNombre(usuarioDetails.getNombre());
        }

        if (usuarioDetails.getApellido() != null && !usuarioDetails.getApellido().isEmpty()) {
            usuarioExistente.setApellido(usuarioDetails.getApellido());
        }

        if (usuarioDetails.getPais() != null) {
            usuarioExistente.setPais(usuarioDetails.getPais());
        }

        if (usuarioDetails.getPassword() != null && !usuarioDetails.getPassword().isEmpty()) {
            usuarioExistente.setPassword(passwordEncoder.encode(usuarioDetails.getPassword()));
        }

        return usuarioRepository.save(usuarioExistente);
    }

    @Transactional
    public void deleteById(Long id) {
        Usuario usuario = findById(id);
        usuario.setEnabled(false);
        usuarioRepository.save(usuario);
    }

}
