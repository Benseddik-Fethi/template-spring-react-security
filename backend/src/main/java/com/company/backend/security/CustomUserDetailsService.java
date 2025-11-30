package com.company.backend.security;

import com.company.backend.domain.User;
import com.company.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Service de chargement des utilisateurs pour Spring Security.
 * <p>
 * Implémente UserDetailsService pour fournir les détails utilisateur
 * lors de l'authentification. Supporte le chargement par email ou par ID.
 * </p>
 *
 * @author Fethi Benseddik
 * @version 1.0
 * @since 2025
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Charge un utilisateur par son email.
     *
     * @param email l'adresse email de l'utilisateur
     * @return les détails de l'utilisateur pour Spring Security
     * @throws UsernameNotFoundException si l'utilisateur n'existe pas
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Utilisateur non trouvé avec l'email: " + email
                ));

        return new CustomUserDetails(user);
    }

    /**
     * Charge un utilisateur par son identifiant.
     * <p>
     * Utilisé principalement par le filtre JWT pour charger l'utilisateur
     * à partir de l'ID extrait du token.
     * </p>
     *
     * @param userId l'identifiant unique de l'utilisateur
     * @return les détails de l'utilisateur pour Spring Security
     * @throws UsernameNotFoundException si l'utilisateur n'existe pas
     */
    @Transactional(readOnly = true)
    public UserDetails loadUserById(UUID userId) throws UsernameNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Utilisateur non trouvé avec l'ID: " + userId
                ));

        return new CustomUserDetails(user);
    }
}