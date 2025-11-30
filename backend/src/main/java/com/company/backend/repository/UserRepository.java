package com.company.backend.repository;

import com.company.backend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository pour l'accès aux données utilisateur.
 * <p>
 * Fournit les méthodes CRUD standard et des requêtes personnalisées
 * pour la gestion des utilisateurs, l'authentification et la protection
 * contre les attaques par force brute.
 * </p>
 *
 * @author Fethi Benseddik
 * @version 1.0
 * @since 2025
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    /**
     * Recherche un utilisateur par son adresse email.
     *
     * @param email l'adresse email
     * @return l'utilisateur s'il existe
     */
    Optional<User> findByEmail(String email);

    /**
     * Vérifie si un email existe déjà en base.
     *
     * @param email l'adresse email à vérifier
     * @return {@code true} si l'email existe
     */
    boolean existsByEmail(String email);

    /**
     * Recherche un utilisateur par son identifiant Google.
     *
     * @param googleId l'identifiant Google
     * @return l'utilisateur s'il existe
     */
    Optional<User> findByGoogleId(String googleId);

    /**
     * Recherche un utilisateur par son identifiant Facebook.
     *
     * @param facebookId l'identifiant Facebook
     * @return l'utilisateur s'il existe
     */
    Optional<User> findByFacebookId(String facebookId);

    /**
     * Réinitialise les tentatives de connexion échouées d'un utilisateur.
     *
     * @param userId l'identifiant de l'utilisateur
     */
    @Modifying
    @Query("UPDATE User u SET u.failedLoginAttempts = 0, u.lockedUntil = null WHERE u.id = :userId")
    void resetFailedLoginAttempts(UUID userId);

    /**
     * Incrémente les tentatives de connexion échouées.
     *
     * @param userId l'identifiant de l'utilisateur
     * @param now    l'horodatage de la tentative
     */
    @Modifying
    @Query("""
                UPDATE User u 
                SET u.failedLoginAttempts = u.failedLoginAttempts + 1, 
                    u.lastFailedLogin = :now 
                WHERE u.id = :userId
            """)
    void incrementFailedLoginAttempts(UUID userId, Instant now);

    /**
     * Verrouille un compte jusqu'à une date donnée.
     *
     * @param userId      l'identifiant de l'utilisateur
     * @param lockedUntil la date de fin du verrouillage
     */
    @Modifying
    @Query("UPDATE User u SET u.lockedUntil = :lockedUntil WHERE u.id = :userId")
    void lockAccount(UUID userId, Instant lockedUntil);
}