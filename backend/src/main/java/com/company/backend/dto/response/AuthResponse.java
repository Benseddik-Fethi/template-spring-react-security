package com.company.backend.dto.response;

/**
 * DTO de réponse d'authentification contenant les tokens JWT.
 * <p>
 * Retourné après une authentification réussie (login, register, refresh).
 * Compatible avec le frontend React utilisant le format camelCase.
 * </p>
 *
 * @param accessToken  le token d'accès JWT (courte durée)
 * @param refreshToken le token de rafraîchissement (longue durée)
 * @param tokenType    le type de token (toujours "Bearer")
 * @param expiresIn    la durée de validité de l'access token en secondes
 * @param user         les informations de l'utilisateur authentifié
 * @author Fethi Benseddik
 * @version 1.0
 * @since 2025
 */
public record AuthResponse(
        String accessToken,
        String refreshToken,
        String tokenType,
        Long expiresIn,
        UserResponse user
) {

    /**
     * Constructeur simplifié avec tokenType "Bearer" par défaut.
     *
     * @param accessToken  le token d'accès JWT
     * @param refreshToken le token de rafraîchissement
     * @param expiresIn    la durée de validité en secondes
     * @param user         les informations utilisateur
     */
    public AuthResponse(String accessToken, String refreshToken, Long expiresIn, UserResponse user) {
        this(accessToken, refreshToken, "Bearer", expiresIn, user);
    }
}