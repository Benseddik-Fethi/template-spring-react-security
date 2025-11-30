package com.company.backend.service;

import com.company.backend.domain.User;

import java.util.Optional;
import java.util.UUID;

/**
 * Interface du service de gestion des tokens JWT.
 * <p>
 * Responsable de la génération, validation et extraction des informations
 * des tokens JWT. Utilise HMAC-SHA256 pour la signature et gère la distinction
 * entre access tokens et refresh tokens.
 * </p>
 *
 * @author Fethi Benseddik
 * @version 1.0
 * @since 2025
 */
public interface JwtService {

    /**
     * Génère un access token pour un utilisateur.
     * <p>
     * L'access token a une durée de vie courte (15 minutes par défaut)
     * et contient les informations de l'utilisateur nécessaires à l'autorisation.
     * </p>
     *
     * @param user l'utilisateur authentifié
     * @return le token d'accès JWT
     */
    String generateAccessToken(User user);

    /**
     * Génère un refresh token pour un utilisateur.
     * <p>
     * Le refresh token a une durée de vie longue (7 jours par défaut)
     * et permet d'obtenir de nouveaux access tokens sans re-authentification.
     * </p>
     *
     * @param user l'utilisateur authentifié
     * @return le token de rafraîchissement JWT
     */
    String generateRefreshToken(User user);

    /**
     * Valide un token JWT.
     *
     * @param token le token à valider
     * @return {@code true} si le token est valide, {@code false} sinon
     */
    boolean isTokenValid(String token);

    /**
     * Extrait l'identifiant de l'utilisateur du token.
     *
     * @param token le token JWT
     * @return l'UUID de l'utilisateur ou empty si invalide
     */
    Optional<UUID> extractUserId(String token);

    /**
     * Extrait l'email de l'utilisateur du token.
     *
     * @param token le token JWT
     * @return l'email de l'utilisateur ou empty si invalide
     */
    Optional<String> extractEmail(String token);

    /**
     * Vérifie si le token est un access token.
     *
     * @param token le token JWT
     * @return {@code true} si c'est un access token, {@code false} si c'est un refresh token
     */
    boolean isAccessToken(String token);

    /**
     * Calcule le hash SHA-256 d'un refresh token pour stockage sécurisé.
     *
     * @param refreshToken le token brut
     * @return le hash hexadécimal du token
     */
    String hashToken(String refreshToken);
}