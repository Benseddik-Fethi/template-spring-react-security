package com.company.backend.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

/**
 * Entité représentant un token de réinitialisation de mot de passe.
 * <p>
 * Utilisé pour permettre aux utilisateurs de réinitialiser leur mot de passe
 * via un lien envoyé par email. Le token expire après 1 heure et est marqué
 * comme utilisé après la réinitialisation pour garantir un usage unique.
 * </p>
 *
 * @author Fethi Benseddik
 * @version 1.0
 * @since 2025
 */
@Entity
@Table(name = "password_reset_tokens", indexes = {
        @Index(name = "idx_reset_token", columnList = "token", unique = true),
        @Index(name = "idx_reset_user", columnList = "user_id"),
        @Index(name = "idx_reset_expires", columnList = "expiresAt")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordResetToken {

    private static final int EXPIRATION_MINUTES = 60;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, length = 36)
    private String token;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Column(nullable = false)
    @Builder.Default
    private Boolean used = false;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    /**
     * Crée un nouveau token de réinitialisation avec la durée d'expiration par défaut.
     *
     * @param user l'utilisateur demandant la réinitialisation
     * @return le token de réinitialisation créé
     */
    public static PasswordResetToken create(User user) {
        return PasswordResetToken.builder()
                .token(UUID.randomUUID().toString())
                .user(user)
                .expiresAt(Instant.now().plus(EXPIRATION_MINUTES, ChronoUnit.MINUTES))
                .used(false)
                .build();
    }

    /**
     * Crée un nouveau token de réinitialisation avec une durée d'expiration personnalisée.
     *
     * @param user              l'utilisateur demandant la réinitialisation
     * @param expirationMinutes durée de validité en minutes
     * @return le token de réinitialisation créé
     */
    public static PasswordResetToken create(User user, int expirationMinutes) {
        return PasswordResetToken.builder()
                .token(UUID.randomUUID().toString())
                .user(user)
                .expiresAt(Instant.now().plus(expirationMinutes, ChronoUnit.MINUTES))
                .used(false)
                .build();
    }

    /**
     * Vérifie si le token a expiré.
     *
     * @return {@code true} si le token est expiré, {@code false} sinon
     */
    public boolean isExpired() {
        return Instant.now().isAfter(expiresAt);
    }

    /**
     * Vérifie si le token est valide (non utilisé et non expiré).
     *
     * @return {@code true} si le token est valide, {@code false} sinon
     */
    public boolean isValid() {
        return !used && !isExpired();
    }

    /**
     * Marque le token comme utilisé pour empêcher sa réutilisation.
     */
    public void markAsUsed() {
        this.used = true;
    }
}