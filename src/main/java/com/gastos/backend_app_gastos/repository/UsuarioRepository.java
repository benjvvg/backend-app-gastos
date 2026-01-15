package com.gastos.backend_app_gastos.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gastos.backend_app_gastos.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByUsername(String username);

}
