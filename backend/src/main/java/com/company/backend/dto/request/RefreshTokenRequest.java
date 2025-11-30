package com.company.backend.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO de requête pour le rafraîchissement des tokens JWT.
 * <p>
 * Contient le refresh token nécessaire pour obtenir un nouveau
 * couple access token / refresh token.
 * </p>
 *
 * @param refreshToken le token de rafraîchissement
 * @author Fethi Benseddik
 * @version 1.0
 * @since 2025
 */
public record RefreshTokenRequest(
        @NotBlank(message = "Le refresh token est obligatoire")
        String refreshToken
) {
}