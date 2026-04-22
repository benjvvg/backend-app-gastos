package com.gastos.backend_app_gastos.service.impl;

import java.time.Instant;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.web.util.WebUtils;

import com.gastos.backend_app_gastos.enums.TokenType;
import com.gastos.backend_app_gastos.exception.TokenException;
import com.gastos.backend_app_gastos.model.RefreshToken;
import com.gastos.backend_app_gastos.model.Usuario;
import com.gastos.backend_app_gastos.repository.RefreshTokenRepository;
import com.gastos.backend_app_gastos.repository.UsuarioRepository;
import com.gastos.backend_app_gastos.request.RefreshTokenRequest;
import com.gastos.backend_app_gastos.response.RefreshTokenResponse;
import com.gastos.backend_app_gastos.service.JwtService;
import com.gastos.backend_app_gastos.service.RefreshTokenService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final UsuarioRepository usuarioRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;

    @Value("${application.security.jwt.refresh-token-expiration}")
    private Long refreshExpiration;

    @Value("${application.security.jwt.refresh-token.cookie-name}")
    private String refreshTokenName;

    @Override
    public RefreshToken createRefreshToken(Long userId) {
        Usuario usuario = usuarioRepository.findById(userId).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        RefreshToken refreshToken = RefreshToken.builder()
                .revoked(false)
                .usuario(usuario)
                .token(Base64.getEncoder().encodeToString(UUID.randomUUID().toString().getBytes()))
                .expiryDate(Instant.now().plusMillis(refreshExpiration))
                .build();
        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token == null) {
            log.error("Token no encontrado");
            throw new TokenException(null, "Token no encontrado");
        }
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenException(token.getToken(), "Token expirado, por favor inicia sesión de nuevo");
        }
        return token;
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Override
    public RefreshTokenResponse generateNewToken(RefreshTokenRequest request) {
        Usuario usuario = refreshTokenRepository.findByToken(request.getRefreshToken())
                .map(this::verifyExpiration)
                .map(RefreshToken::getUsuario)
                .orElseThrow(() -> new TokenException(request.getRefreshToken(), "Token no válido"));

        String token = jwtService.generateToken(usuario);
        return RefreshTokenResponse.builder()
                .accessToken(token)
                .refreshToken(request.getRefreshToken())
                .tokenType(TokenType.BEARER.name())
                .build();
    }

    @Override
    public ResponseCookie generateRefreshTokenCookie(String token) {
        return ResponseCookie.from(refreshTokenName, token)
                .path("/")
                .maxAge(refreshExpiration / 1000)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .build();
    }

    @Override
    public String getRefreshTokenFromCookies(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, refreshTokenName);
        if (cookie != null) {
            return cookie.getValue();
        } else {
            return "";
        }
    }

    @Override
    public void deleteByToken(String token) {
        refreshTokenRepository.findByToken(token).ifPresent(refreshTokenRepository::delete);
    }

    @Override
    public ResponseCookie getCleanRefreshTokenCookie() {
        return ResponseCookie.from(refreshTokenName, "")
            .path("/")
            .httpOnly(true)
            .maxAge(0)
            .build();
    }
}
