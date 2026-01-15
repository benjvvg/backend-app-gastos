package com.gastos.backend_app_gastos.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.gastos.backend_app_gastos.jwt.JwtService;
import com.gastos.backend_app_gastos.model.Rol;
import com.gastos.backend_app_gastos.model.Usuario;
import com.gastos.backend_app_gastos.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        UserDetails usuario=usuarioRepository.findByUsername(request.getUsername()).orElseThrow();
        String token=jwtService.getToken(usuario);
        return AuthResponse.builder()
            .token(token)
            .build();

    }

    public AuthResponse register(RegisterRequest request) {
        Usuario usuario = Usuario.builder()
            .username(request.getUsername())
            .password(passwordEncoder.encode( request.getPassword()))
            .nombre(request.getNombre())
            .apellido(request.getApellido())
            .pais(request.getPais())
            .rol(Rol.USER)
            .build();

        usuarioRepository.save(usuario);

        return AuthResponse.builder()
            .token(jwtService.getToken(usuario))
            .build();
        
    }
}
