package com.company.backend.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO de requête pour l'échange du code d'autorisation OAuth2 contre les tokens.
 * <p>
 * Après une authentification OAuth2 réussie, le frontend reçoit un code
 * d'autorisation temporaire qu'il échange contre les tokens JWT via cet endpoint.
 * </p>
 *
 * @param code le code d'autorisation OAuth2 à échanger
 * @author Fethi Benseddik
 * @version 1.0
 * @since 2025
 */
public record OAuthCodeExchangeRequest(
        @NotBlank(message = "Le code d'autorisation est obligatoire")
        String code
) {
}