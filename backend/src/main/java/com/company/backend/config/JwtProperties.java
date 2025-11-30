package com.company.backend.config;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;

/**
 * Propriétés de configuration pour les tokens JWT.
 * <p>
 * Chargées depuis le fichier application.yml sous le préfixe "jwt".
 * La clé secrète est obligatoire et doit faire au minimum 256 bits.
 * </p>
 *
 * @param secret       la clé secrète pour signer les tokens (obligatoire)
 * @param accessToken  configuration du token d'accès
 * @param refreshToken configuration du token de rafraîchissement
 * @param issuer       l'émetteur des tokens (claim "iss")
 * @param audience     l'audience des tokens (claim "aud")
 * @author Fethi Benseddik
 * @version 1.0
 * @since 2025
 */
@ConfigurationProperties(prefix = "jwt")
@Validated
public record JwtProperties(
        @NotBlank(message = "JWT secret is required")
        String secret,

        AccessToken accessToken,
        RefreshToken refreshToken,

        String issuer,
        String audience
) {

    public JwtProperties {
        if (accessToken == null) {
            accessToken = new AccessToken(Duration.ofMinutes(15));
        }
        if (refreshToken == null) {
            refreshToken = new RefreshToken(Duration.ofDays(7));
        }
        if (issuer == null || issuer.isBlank()) {
            issuer = "template-api";
        }
        if (audience == null || audience.isBlank()) {
            audience = "template-app";
        }
    }

    /**
     * Configuration du token d'accès.
     *
     * @param expiration durée de validité (15 minutes par défaut)
     */
    public record AccessToken(Duration expiration) {
        public AccessToken {
            if (expiration == null) {
                expiration = Duration.ofMinutes(15);
            }
        }
    }

    /**
     * Configuration du token de rafraîchissement.
     *
     * @param expiration durée de validité (7 jours par défaut)
     */
    public record RefreshToken(Duration expiration) {
        public RefreshToken {
            if (expiration == null) {
                expiration = Duration.ofDays(7);
            }
        }
    }
}