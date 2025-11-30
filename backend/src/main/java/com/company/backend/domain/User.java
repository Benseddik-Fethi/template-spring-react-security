package com.company.backend.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Entité représentant un utilisateur de l'application.
 * <p>
 * Cette entité gère les informations d'identification, les données de profil,
 * les paramètres de sécurité (verrouillage de compte, tentatives de connexion)
 * et les relations avec les sessions et les logs d'audit.
 * </p>
 *
 * @author Fethi Benseddik
 * @version 1.0
 * @since 2025
 */
@Entity
@Table(name = "users", indexes = {
        @Index(name = "idx_user_email", columnList = "email", unique = true),
        @Index(name = "idx_user_google_id", columnList = "googleId"),
        @Index(name = "idx_user_facebook_id", columnList = "facebookId")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "password_hash")
    private String passwordHash;

    @Column(name = "first_name", length = 100)
    private String firstName;

    @Column(name = "last_name", length = 100)
    private String lastName;

    @Column(length = 500)
    private String avatar;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private Role role = Role.USER;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private AuthProvider provider = AuthProvider.EMAIL;

    @Column(name = "google_id", length = 100)
    private String googleId;

    @Column(name = "facebook_id", length = 100)
    private String facebookId;

    @Column(name = "email_verified")
    @Builder.Default
    private Boolean emailVerified = false;

    @Column(name = "failed_login_attempts")
    @Builder.Default
    private Integer failedLoginAttempts = 0;

    @Column(name = "last_failed_login")
    private Instant lastFailedLogin;

    @Column(name = "locked_until")
    private Instant lockedUntil;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Session> sessions = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<AuditLog> auditLogs = new ArrayList<>();

    @Column(name = "preferred_language", length = 5, nullable = false)
    @Builder.Default
    private String preferredLanguage = "fr";

    /**
     * Vérifie si le compte utilisateur est actuellement verrouillé.
     *
     * @return {@code true} si le compte est verrouillé, {@code false} sinon
     */
    public boolean isAccountLocked() {
        return lockedUntil != null && Instant.now().isBefore(lockedUntil);
    }

    /**
     * Enregistre une tentative de connexion échouée et verrouille le compte
     * si le nombre maximum de tentatives est atteint.
     *
     * @param maxAttempts         nombre maximum de tentatives avant verrouillage
     * @param lockDurationMinutes durée du verrouillage en minutes
     */
    public void recordFailedLogin(int maxAttempts, int lockDurationMinutes) {
        this.failedLoginAttempts = (this.failedLoginAttempts == null ? 0 : this.failedLoginAttempts) + 1;
        this.lastFailedLogin = Instant.now();

        if (this.failedLoginAttempts >= maxAttempts) {
            this.lockedUntil = Instant.now().plusSeconds(lockDurationMinutes * 60L);
        }
    }

    /**
     * Réinitialise les compteurs de tentatives de connexion échouées
     * après une connexion réussie.
     */
    public void resetFailedLoginAttempts() {
        this.failedLoginAttempts = 0;
        this.lastFailedLogin = null;
        this.lockedUntil = null;
    }

    /**
     * Retourne le nom complet de l'utilisateur.
     *
     * @return le nom complet ou l'email si aucun nom n'est défini
     */
    public String getFullName() {
        if (firstName == null && lastName == null) {
            return email;
        }
        return ((firstName != null ? firstName : "") + " " + (lastName != null ? lastName : "")).trim();
    }
}