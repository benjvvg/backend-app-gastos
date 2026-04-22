package com.gastos.backend_app_gastos.service;

import com.gastos.backend_app_gastos.request.AuthenticationRequest;
import com.gastos.backend_app_gastos.request.RegisterRequest;
import com.gastos.backend_app_gastos.response.AuthResponse;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse authenticate(AuthenticationRequest request);
}
