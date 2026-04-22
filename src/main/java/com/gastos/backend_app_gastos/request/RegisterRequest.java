package com.gastos.backend_app_gastos.request;


import com.gastos.backend_app_gastos.enums.Rol;
import com.gastos.backend_app_gastos.validation.StrongPassword;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email no es válido")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @StrongPassword
    private String password;

    @NotBlank(message = "El país es obligatorio")
    private String pais;

    @NotNull
    private Rol rol;
}
