package com.gastos.backend_app_gastos.service.impl;

import com.gastos.backend_app_gastos.enums.TokenType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import com.gastos.backend_app_gastos.enums.Rol;
import com.gastos.backend_app_gastos.model.Usuario;
import com.gastos.backend_app_gastos.repository.UsuarioRepository;
import com.gastos.backend_app_gastos.request.AuthenticationRequest;
import com.gastos.backend_app_gastos.request.RegisterRequest;
import com.gastos.backend_app_gastos.response.AuthResponse;
import com.gastos.backend_app_gastos.service.AuthService;
import com.gastos.backend_app_gastos.service.JwtService;
import com.gastos.backend_app_gastos.service.RefreshTokenService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthService {

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;

    @Override
    public AuthResponse register(RegisterRequest request) {
        var usuario = Usuario.builder()
                .email(request.getEmail())
                .nombre(request.getNombre())
                .apellido(request.getApellido())
                .pais(request.getPais())
                .password(passwordEncoder.encode(request.getPassword()))
                .rol(Rol.USER)
                .build();
        usuario = usuarioRepository.save(usuario);
        var jwt = jwtService.generateToken(usuario);
        var refreshToken = refreshTokenService.createRefreshToken(usuario.getId());
        var roles = usuario.getRol().getAuthorities()
                .stream()
                .map(SimpleGrantedAuthority::getAuthority)
                .toList();

        return AuthResponse.builder()
                .accessToken(jwt)
                .email(usuario.getEmail())
                .id(usuario.getId())
                .refreshToken(refreshToken.getToken())
                .roles(roles)
                .tokenType(TokenType.BEARER.name())
                .build();
    }

    @Override
    public AuthResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        var usuario = usuarioRepository.findByEmail(request.getEmail()).orElseThrow(() -> new IllegalArgumentException("Email o contraseña incorrectas"));
        var roles = usuario.getRol().getAuthorities()
                .stream()
                .map(SimpleGrantedAuthority::getAuthority)
                .toList();
        var jwt = jwtService.generateToken(usuario);
        var refreshToken = refreshTokenService.createRefreshToken(usuario.getId());
        return AuthResponse.builder()
                .accessToken(jwt)
                .roles(roles)
                .email(usuario.getEmail())
                .id(usuario.getId())
                .refreshToken(refreshToken.getToken())
                .tokenType(TokenType.BEARER.name())
                .build();

    }
}
