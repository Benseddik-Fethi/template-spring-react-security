package com.company.backend.repository;

import com.company.backend.domain.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository pour l'accès aux tokens de vérification d'email.
 * <p>
 * Gère les tokens temporaires utilisés pour la vérification d'adresse email.
 * Utilise des JOIN FETCH pour optimiser le chargement des utilisateurs.
 * </p>
 *
 * @author Fethi Benseddik
 * @version 1.0
 * @since 2025
 */
@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, UUID> {

    /**
     * Recherche un token valide (non expiré).
     *
     * @param token le token à rechercher
     * @param now   l'instant actuel
     * @return le token s'il est valide
     */
    @Query("""
                SELECT t FROM VerificationToken t 
                JOIN FETCH t.user
                WHERE t.token = :token 
                AND t.expiresAt > :now
            """)
    Optional<VerificationToken> findValidByToken(String token, Instant now);

    /**
     * Recherche le token d'un utilisateur.
     *
     * @param userId l'identifiant de l'utilisateur
     * @return le token s'il existe
     */
    Optional<VerificationToken> findByUserId(UUID userId);

    /**
     * Supprime le token d'un utilisateur.
     *
     * @param userId l'identifiant de l'utilisateur
     */
    @Modifying
    @Query("DELETE FROM VerificationToken t WHERE t.user.id = :userId")
    void deleteByUserId(UUID userId);

    /**
     * Supprime les tokens expirés (nettoyage périodique).
     *
     * @param now l'instant actuel
     * @return le nombre de tokens supprimés
     */
    @Modifying
    @Query("DELETE FROM VerificationToken t WHERE t.expiresAt < :now")
    int deleteExpiredTokens(Instant now);
}