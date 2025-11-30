package com.company.backend.repository;

import com.company.backend.domain.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository pour l'accès aux tokens de réinitialisation de mot de passe.
 * <p>
 * Gère les tokens temporaires utilisés pour la réinitialisation de mot de passe.
 * Utilise des JOIN FETCH pour optimiser le chargement des utilisateurs.
 * </p>
 *
 * @author Fethi Benseddik
 * @version 1.0
 * @since 2025
 */
@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, UUID> {

    /**
     * Recherche un token valide (non expiré et non utilisé).
     *
     * @param token le token à rechercher
     * @param now   l'instant actuel
     * @return le token s'il est valide
     */
    @Query("""
                SELECT t FROM PasswordResetToken t 
                JOIN FETCH t.user
                WHERE t.token = :token 
                AND t.used = false 
                AND t.expiresAt > :now
            """)
    Optional<PasswordResetToken> findValidByToken(String token, Instant now);

    /**
     * Recherche le dernier token d'un utilisateur.
     *
     * @param userId l'identifiant de l'utilisateur
     * @return le dernier token créé
     */
    @Query("SELECT t FROM PasswordResetToken t WHERE t.user.id = :userId ORDER BY t.createdAt DESC")
    Optional<PasswordResetToken> findLatestByUserId(UUID userId);

    /**
     * Invalide tous les tokens d'un utilisateur.
     *
     * @param userId l'identifiant de l'utilisateur
     * @return le nombre de tokens invalidés
     */
    @Modifying
    @Query("UPDATE PasswordResetToken t SET t.used = true WHERE t.user.id = :userId AND t.used = false")
    int invalidateAllUserTokens(UUID userId);

    /**
     * Supprime les tokens expirés (nettoyage périodique).
     *
     * @param now l'instant actuel
     * @return le nombre de tokens supprimés
     */
    @Modifying
    @Query("DELETE FROM PasswordResetToken t WHERE t.expiresAt < :now")
    int deleteExpiredTokens(Instant now);

    /**
     * Supprime les tokens déjà utilisés.
     *
     * @return le nombre de tokens supprimés
     */
    @Modifying
    @Query("DELETE FROM PasswordResetToken t WHERE t.used = true")
    int deleteUsedTokens();
}