package com.company.backend.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

/**
 * Entité représentant un code d'autorisation temporaire pour OAuth2.
 * <p>
 * Ce code est généré après une authentification OAuth2 réussie et permet
 * au frontend d'échanger ce code contre les tokens d'accès. Le code expire
 * après 30 secondes et est supprimé après utilisation pour garantir la sécurité.
 * </p>
 *
 * @author Fethi Benseddik
 * @version 1.0
 * @since 2025
 */
@Entity
@Table(name = "oauth_authorization_codes", indexes = {
        @Index(name = "idx_oauth_code", columnList = "code", unique = true),
        @Index(name = "idx_oauth_expires", columnList = "expiresAt")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OAuthAuthorizationCode {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, length = 64)
    private String code;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "access_token", nullable = false, length = 1000)
    private String accessToken;

    @Column(name = "refresh_token", nullable = false, length = 1000)
    private String refreshToken;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Column(nullable = false)
    @Builder.Default
    private Boolean used = false;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    /**
     * Crée un nouveau code d'autorisation avec expiration dans 30 secondes.
     *
     * @param user         l'utilisateur authentifié
     * @param accessToken  le token d'accès pré-généré
     * @param refreshToken le token de rafraîchissement pré-généré
     * @return le code d'autorisation créé
     */
    public static OAuthAuthorizationCode create(User user, String accessToken, String refreshToken) {
        return OAuthAuthorizationCode.builder()
                .code(UUID.randomUUID().toString().replace("-", ""))
                .user(user)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiresAt(Instant.now().plusSeconds(30))
                .used(false)
                .build();
    }

    /**
     * Vérifie si le code est valide (non expiré et non utilisé).
     *
     * @return {@code true} si le code est valide, {@code false} sinon
     */
    public boolean isValid() {
        return !used && Instant.now().isBefore(expiresAt);
    }

    /**
     * Marque le code comme utilisé pour empêcher sa réutilisation.
     */
    public void markAsUsed() {
        this.used = true;
    }
}