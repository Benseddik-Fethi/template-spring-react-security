package com.company.backend.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Entité représentant un journal d'audit des actions sensibles.
 * <p>
 * Enregistre toutes les actions importantes effectuées dans l'application
 * pour des raisons de sécurité et de traçabilité : connexions, déconnexions,
 * changements de mot de passe, verrouillages de compte, etc.
 * </p>
 *
 * @author Fethi Benseddik
 * @version 1.0
 * @since 2025
 */
@Entity
@Table(name = "audit_logs", indexes = {
        @Index(name = "idx_audit_user", columnList = "user_id"),
        @Index(name = "idx_audit_action", columnList = "action"),
        @Index(name = "idx_audit_created", columnList = "createdAt")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false, length = 100)
    private String action;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    @Builder.Default
    private Map<String, Object> metadata = new HashMap<>();

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(name = "user_agent", length = 500)
    private String userAgent;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    /**
     * Crée un log d'audit pour une connexion réussie.
     *
     * @param user      l'utilisateur connecté
     * @param ipAddress l'adresse IP du client
     * @param userAgent le User-Agent du navigateur
     * @return le log d'audit créé
     */
    public static AuditLog loginSuccess(User user, String ipAddress, String userAgent) {
        return AuditLog.builder()
                .user(user)
                .action("LOGIN_SUCCESS")
                .ipAddress(ipAddress)
                .userAgent(userAgent)
                .metadata(Map.of("email", user.getEmail()))
                .build();
    }

    /**
     * Crée un log d'audit pour une tentative de connexion échouée.
     *
     * @param email     l'email utilisé pour la tentative
     * @param ipAddress l'adresse IP du client
     * @param userAgent le User-Agent du navigateur
     * @param reason    la raison de l'échec
     * @return le log d'audit créé
     */
    public static AuditLog loginFailed(String email, String ipAddress, String userAgent, String reason) {
        return AuditLog.builder()
                .action("LOGIN_FAILED")
                .ipAddress(ipAddress)
                .userAgent(userAgent)
                .metadata(Map.of("email", email, "reason", reason))
                .build();
    }

    /**
     * Crée un log d'audit pour une déconnexion.
     *
     * @param user      l'utilisateur déconnecté
     * @param ipAddress l'adresse IP du client
     * @return le log d'audit créé
     */
    public static AuditLog logout(User user, String ipAddress) {
        return AuditLog.builder()
                .user(user)
                .action("LOGOUT")
                .ipAddress(ipAddress)
                .metadata(Map.of("email", user.getEmail()))
                .build();
    }

    /**
     * Crée un log d'audit pour un verrouillage de compte.
     *
     * @param user      l'utilisateur dont le compte est verrouillé
     * @param ipAddress l'adresse IP du client
     * @return le log d'audit créé
     */
    public static AuditLog accountLocked(User user, String ipAddress) {
        return AuditLog.builder()
                .user(user)
                .action("ACCOUNT_LOCKED")
                .ipAddress(ipAddress)
                .metadata(Map.of(
                        "email", user.getEmail(),
                        "lockedUntil", user.getLockedUntil().toString()
                ))
                .build();
    }

    /**
     * Crée un log d'audit pour un changement de mot de passe.
     *
     * @param user   l'utilisateur ayant changé son mot de passe
     * @param method la méthode utilisée (user_change, password_reset, etc.)
     * @return le log d'audit créé
     */
    public static AuditLog passwordChanged(User user, String method) {
        return AuditLog.builder()
                .user(user)
                .action("PASSWORD_CHANGED")
                .metadata(Map.of("email", user.getEmail(), "method", method))
                .build();
    }

    /**
     * Crée un log d'audit pour une connexion OAuth2.
     *
     * @param user      l'utilisateur connecté
     * @param provider  le fournisseur OAuth2 utilisé
     * @param ipAddress l'adresse IP du client
     * @param userAgent le User-Agent du navigateur
     * @return le log d'audit créé
     */
    public static AuditLog oauthLogin(User user, AuthProvider provider, String ipAddress, String userAgent) {
        return AuditLog.builder()
                .user(user)
                .action("OAUTH_LOGIN")
                .ipAddress(ipAddress)
                .userAgent(userAgent)
                .metadata(Map.of("email", user.getEmail(), "provider", provider.name()))
                .build();
    }

    /**
     * Crée un log d'audit pour une vérification d'email réussie.
     *
     * @param user l'utilisateur ayant vérifié son email
     * @return le log d'audit créé
     */
    public static AuditLog emailVerified(User user) {
        return AuditLog.builder()
                .user(user)
                .action("EMAIL_VERIFIED")
                .metadata(Map.of("email", user.getEmail()))
                .build();
    }

    /**
     * Crée un log d'audit pour une demande de réinitialisation de mot de passe.
     *
     * @param email     l'email pour lequel la réinitialisation est demandée
     * @param ipAddress l'adresse IP du client
     * @return le log d'audit créé
     */
    public static AuditLog passwordResetRequested(String email, String ipAddress) {
        return AuditLog.builder()
                .action("PASSWORD_RESET_REQUESTED")
                .ipAddress(ipAddress)
                .metadata(Map.of("email", email))
                .build();
    }
}