package com.gastos.backend_app_gastos.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gastos.backend_app_gastos.model.Categoria;
import com.gastos.backend_app_gastos.model.Usuario;
import com.gastos.backend_app_gastos.repository.CategoriaRepository;
import com.gastos.backend_app_gastos.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional(readOnly = true)
    public List<Categoria> listarPorUsuario(String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return categoriaRepository.findByUsuarioId(usuario.getId());
    }

    @Transactional
    public Categoria crearCategoria(Categoria categoria, String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        categoria.setUsuario(usuario);
        return categoriaRepository.save(categoria);
    }

    @Transactional
    public Categoria actualizarCategoria(Integer id, Categoria categoriaDetails) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria no encontrada"));
        
        categoria.setNombre(categoriaDetails.getNombre());
        categoria.setColor(categoriaDetails.getColor());
        categoria.setIcono(categoriaDetails.getIcono());

        return categoriaRepository.save(categoria);
    }

    @Transactional
    public void eliminarCategoria(Integer id) {
        categoriaRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Categoria findById(Integer id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria no encontrada"));
    }
}
