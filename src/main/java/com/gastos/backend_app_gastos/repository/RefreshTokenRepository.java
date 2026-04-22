package com.gastos.backend_app_gastos.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.gastos.backend_app_gastos.model.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

}
