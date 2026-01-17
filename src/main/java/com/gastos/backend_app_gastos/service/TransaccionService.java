package com.gastos.backend_app_gastos.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gastos.backend_app_gastos.model.Categoria;
import com.gastos.backend_app_gastos.model.Transaccion;
import com.gastos.backend_app_gastos.model.Usuario;
import com.gastos.backend_app_gastos.repository.CategoriaRepository;
import com.gastos.backend_app_gastos.repository.TransaccionRepository;
import com.gastos.backend_app_gastos.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransaccionService {
    private final TransaccionRepository transaccionRepository;
    private final UsuarioRepository usuarioRepository;
    private final CategoriaRepository categoriaRepository;

    @Transactional(readOnly = true)
    public List<Transaccion> listarPorUsuario(String username) {
        Usuario usuario = obtenerUsuario(username);
        return transaccionRepository.findByUsuarioId(usuario.getId());
    }

    @Transactional
    public Transaccion crearTransaccion(Transaccion transaccion, String username) {
        Usuario usuario = obtenerUsuario(username);
        transaccion.setUsuario(usuario);

        if (transaccion.getCategoria() != null && transaccion.getCategoria().getId() != null) {
            Categoria categoria = categoriaRepository.findById(transaccion.getCategoria().getId())
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
            transaccion.setCategoria(categoria);
        }

        return transaccionRepository.save(transaccion);
    }

    @Transactional
    public void eliminarTransaccion(Long id) {
        transaccionRepository.deleteById(id);
    }

    private Usuario obtenerUsuario(String username) {
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
}
