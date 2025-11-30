package com.company.backend.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

/**
 * Entité représentant un compte OAuth2 lié à un utilisateur.
 * <p>
 * Permet à un utilisateur d'avoir plusieurs méthodes de connexion
 * (email + Google + Facebook par exemple). Chaque compte OAuth2
 * est identifié par le fournisseur et l'ID unique chez ce fournisseur.
 * </p>
 *
 * @author Fethi Benseddik
 * @version 1.0
 * @since 2025
 */
@Entity
@Table(name = "accounts",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_account_provider_id",
                columnNames = {"provider", "provider_account_id"}
        ),
        indexes = {
                @Index(name = "idx_account_user", columnList = "user_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AuthProvider provider;

    @Column(name = "provider_account_id", nullable = false, length = 255)
    private String providerAccountId;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
}