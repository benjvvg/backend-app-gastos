package com.gastos.backend_app_gastos.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gastos.backend_app_gastos.model.Transaccion;
import com.gastos.backend_app_gastos.service.TransaccionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/transacciones")
@RequiredArgsConstructor
public class TransaccionController {

    private final TransaccionService transaccionService;

    @GetMapping
    public ResponseEntity<List<Transaccion>> listarMisTransacciones(Principal principal) {
        return ResponseEntity.ok(transaccionService.listarPorUsuario(principal.getName()));
    }

    @PostMapping
    public ResponseEntity<Transaccion> crear(@RequestBody Transaccion transaccion, Principal principal) {
        return ResponseEntity.ok(transaccionService.crearTransaccion(transaccion, principal.getName()));
    }

    @DeleteMapping("/{id}")
        public ResponseEntity<Void> eliminar(@PathVariable Long id) {
            transaccionService.eliminarTransaccion(id);
            return ResponseEntity.noContent().build();
        }
}
