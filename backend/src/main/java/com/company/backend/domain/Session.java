package com.company.backend.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

/**
 * Entité représentant une session utilisateur avec son refresh token.
 * <p>
 * Stocke le hash SHA-256 du refresh token pour permettre la validation
 * et la révocation des sessions. Le token brut n'est jamais stocké
 * pour des raisons de sécurité.
 * </p>
 *
 * @author Fethi Benseddik
 * @version 1.0
 * @since 2025
 */
@Entity
@Table(name = "sessions", indexes = {
        @Index(name = "idx_session_user", columnList = "user_id"),
        @Index(name = "idx_session_token_hash", columnList = "refreshTokenHash"),
        @Index(name = "idx_session_expires", columnList = "expiresAt")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "refresh_token_hash", nullable = false, length = 64)
    private String refreshTokenHash;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(name = "user_agent", length = 500)
    private String userAgent;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Column(name = "revoked_at")
    private Instant revokedAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    /**
     * Vérifie si la session est valide (non révoquée et non expirée).
     *
     * @return {@code true} si la session est valide, {@code false} sinon
     */
    public boolean isValid() {
        return revokedAt == null && Instant.now().isBefore(expiresAt);
    }

    /**
     * Révoque la session en enregistrant la date de révocation.
     */
    public void revoke() {
        this.revokedAt = Instant.now();
    }

    /**
     * Vérifie si la session a été révoquée.
     *
     * @return {@code true} si la session est révoquée, {@code false} sinon
     */
    public boolean isRevoked() {
        return revokedAt != null;
    }

    /**
     * Vérifie si la session a expiré.
     *
     * @return {@code true} si la session est expirée, {@code false} sinon
     */
    public boolean isExpired() {
        return Instant.now().isAfter(expiresAt);
    }
}