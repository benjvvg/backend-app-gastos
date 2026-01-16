package com.gastos.backend_app_gastos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gastos.backend_app_gastos.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer>{
    List<Categoria> findByUsuarioId(Integer idUsuario);
}
