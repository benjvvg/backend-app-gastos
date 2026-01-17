package com.gastos.backend_app_gastos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gastos.backend_app_gastos.model.Transaccion;
import java.time.LocalDate;


public interface TransaccionRepository extends JpaRepository<Transaccion, Long> {
    List<Transaccion> findByUsuarioId(Integer usuarioId);

    List<Transaccion> findByUsuarioIdAndFechaBetween(Integer userId, LocalDate fechaInicial, LocalDate fechaFinal);
}
