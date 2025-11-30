package com.company.backend.security;

import com.company.backend.domain.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Implémentation de UserDetails pour Spring Security.
 * <p>
 * Encapsule l'entité User pour l'authentification et l'autorisation
 * dans le contexte Spring Security.
 * </p>
 *
 * @author Fethi Benseddik
 * @version 1.0
 * @since 2025
 */
@RequiredArgsConstructor
@Getter
public class CustomUserDetails implements UserDetails {

    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
    }

    @Override
    public String getPassword() {
        return user.getPasswordHash();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !user.isAccountLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * Récupère l'identifiant unique de l'utilisateur.
     *
     * @return l'UUID de l'utilisateur
     */
    public UUID getId() {
        return user.getId();
    }

    /**
     * Récupère l'adresse email de l'utilisateur.
     *
     * @return l'email de l'utilisateur
     */
    public String getEmail() {
        return user.getEmail();
    }
}