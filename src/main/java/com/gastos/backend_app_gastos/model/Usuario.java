package com.gastos.backend_app_gastos.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.gastos.backend_app_gastos.enums.Rol;

import java.util.Collection;

import jakarta.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="usuarios")
public class Usuario implements UserDetails{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, length = 30)
    private String nombre;

    @Column(nullable = false, length = 30)
    private String apellido;

    @Column(length = 30, nullable = false)
    private String pais; 

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING) 
    private Rol rol;

    @Column(columnDefinition = "boolean default true")
    @Builder.Default
    private boolean enabled = true;

    //Columna futuras OAuth2.0
    /* 
    private String googleId;
    private String googleRefreshToken; */

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return rol.getAuthorities();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return true;
    }
}
