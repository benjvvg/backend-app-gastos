package com.gastos.backend_app_gastos.service;

import java.util.Optional;

import org.springframework.http.ResponseCookie;

import com.gastos.backend_app_gastos.model.RefreshToken;
import com.gastos.backend_app_gastos.request.RefreshTokenRequest;
import com.gastos.backend_app_gastos.response.RefreshTokenResponse;

import jakarta.servlet.http.HttpServletRequest;

public interface RefreshTokenService {
    RefreshToken createRefreshToken(Long userId);
    RefreshToken verifyExpiration(RefreshToken token);
    Optional<RefreshToken> findByToken(String token);
    RefreshTokenResponse generateNewToken(RefreshTokenRequest request);
    ResponseCookie generateRefreshTokenCookie(String token);
    String getRefreshTokenFromCookies(HttpServletRequest request);
    void deleteByToken(String token);
    ResponseCookie getCleanRefreshTokenCookie();
}
