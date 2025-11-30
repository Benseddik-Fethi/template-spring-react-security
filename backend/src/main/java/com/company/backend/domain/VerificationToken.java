package com.company.backend.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

/**
 * Entité représentant un token de vérification d'adresse email.
 * <p>
 * Utilisé pour confirmer l'adresse email d'un utilisateur lors de
 * l'inscription. Le token expire après 24 heures et est supprimé
 * après utilisation pour garantir un usage unique.
 * </p>
 *
 * @author Fethi Benseddik
 * @version 1.0
 * @since 2025
 */
@Entity
@Table(name = "verification_tokens", indexes = {
        @Index(name = "idx_verification_token", columnList = "token", unique = true),
        @Index(name = "idx_verification_user", columnList = "user_id"),
        @Index(name = "idx_verification_expires", columnList = "expiresAt")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerificationToken {

    private static final int EXPIRATION_HOURS = 24;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, length = 36)
    private String token;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    /**
     * Crée un nouveau token de vérification avec la durée d'expiration par défaut.
     *
     * @param user l'utilisateur associé au token
     * @return le token de vérification créé
     */
    public static VerificationToken create(User user) {
        return VerificationToken.builder()
                .token(UUID.randomUUID().toString())
                .user(user)
                .expiresAt(Instant.now().plus(EXPIRATION_HOURS, ChronoUnit.HOURS))
                .build();
    }

    /**
     * Crée un nouveau token de vérification avec une durée d'expiration personnalisée.
     *
     * @param user            l'utilisateur associé au token
     * @param expirationHours durée de validité en heures
     * @return le token de vérification créé
     */
    public static VerificationToken create(User user, int expirationHours) {
        return VerificationToken.builder()
                .token(UUID.randomUUID().toString())
                .user(user)
                .expiresAt(Instant.now().plus(expirationHours, ChronoUnit.HOURS))
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
     * Vérifie si le token est valide (non expiré).
     *
     * @return {@code true} si le token est valide, {@code false} sinon
     */
    public boolean isValid() {
        return !isExpired();
    }
}